package faang.school.achievement.infrastructure.event;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.EventHandler;
import faang.school.achievement.infrastructure.executor.HandlersExecutionStrategy;
import faang.school.achievement.infrastructure.kafka.HandlerRetry;
import faang.school.achievement.infrastructure.kafka.dlq.DlqMessageSender;
import faang.school.achievement.infrastructure.store.RetryCountStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RetryableEventProcessingService<T, H extends EventHandler<T>> implements EventProcessingService<T, H> {

    private final List<H> handlers;
    private final HandlerRetry handlerRetry;
    private final RetryCountStore<T, H> retryCountStore;
    private final HandlersExecutionStrategy<T, H> handlersExecutionStrategy;
    private final DlqMessageSender dlqMessageSender;
    private final long maxRetryCount;
    private final String dlqTopicName;

    private List<H> getHandlersByExceptions(List<HandlerException> handlerExceptions) {
        List<String> handlerNames = handlerExceptions.stream().map(HandlerException::getHandlerName).toList();
        return handlers.stream().filter(handler -> handlerNames.contains(handler.getClass().getSimpleName())).toList();
    }

    @Override
    public void process(String eventKey, List<H> handlers, T event, Acknowledgment ack) throws HandlersException {
        ack.acknowledge();

        List<H> currentHandlers = new ArrayList<>();
        try {
            if (retryCountStore.getRetryCount(eventKey) == 0) {
                currentHandlers.addAll(handlers);
            } else {
                currentHandlers.addAll(retryCountStore.getUnworkedHandlers(eventKey));
            }

            handlerRetry.execute(() -> handlersExecutionStrategy.processHandlers(currentHandlers, event));
        } catch (HandlersException e) {
            retryCountStore.incrementRetryCount(eventKey);

            List<H> errorHandlers = getHandlersByExceptions(e.getHandlerExceptions());
            retryCountStore.setUnworkedHandlers(eventKey, errorHandlers);

            long currentRetryCount = retryCountStore.getRetryCount(eventKey);

            if (currentRetryCount == maxRetryCount) {
                Map<String, String> errorMessages = e.getHandlerExceptions().stream()
                    .collect(Collectors.toMap(HandlerException::getHandlerName, HandlerException::getMessage));

                dlqMessageSender.send(event, errorMessages.keySet(), currentRetryCount, errorMessages, eventKey,
                                      dlqTopicName);

                retryCountStore.clearRetryState(eventKey);
            } else {
                throw e;
            }
        }
    }
}