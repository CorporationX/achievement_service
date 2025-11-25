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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryableEventProcessingService<T, H extends EventHandler<T>> implements EventProcessingService<T, H> {

    protected final List<H> handlers;
    protected final HandlerRetry handlerRetry;
    @Qualifier("InMemoryRetryCountStore")
    protected final RetryCountStore<T, H> retryCountStore;
    @Qualifier("invokeAllHandlersExecutionStrategy")
    protected final HandlersExecutionStrategy<T, H> handlersExecutionStrategy;
    protected final DlqMessageSender dlqMessageSender;
    @Value("${app.kafka.retryable-topic.retry:3}")
    protected long countRetryableTopic;
    @Value("${app.kafka.topics.comment-create-events-dlq:comment-create-events-dlq}")
    String topicDlqName;

    private List<H> getObjectsByTheirNames(List<HandlerException> handlerExceptions) {
        List<String> manesHandlers = handlerExceptions.stream()
                                                      .map(HandlerException::getHandlerName)
                                                      .toList();

        return handlers.stream()
                       .filter(handler -> manesHandlers.contains(handler.getClass().getSimpleName()))
                       .toList();
    }

    /**
     * Общая логика обработки события: - выбираем, какие хэндлеры должны отработать (первые / недоработанные) -
     * запускаем их через HandlerRetry - при успехе делаем ack
     *
     * @param eventKey ключ события
     * @param handlers все хэндлеры для данного события
     * @param event    само событие
     * @param ack      kafka ack
     */
    public void process(String eventKey, List<H> handlers, T event, Acknowledgment ack) throws HandlersException {
        ack.acknowledge();
        List<H> handlersCurrent = new ArrayList<>();
        try {
            if (retryCountStore.getRetryCount(eventKey) == 0) {
                handlersCurrent.addAll(handlers);
            } else {
                handlersCurrent.addAll(retryCountStore.getUnworkedHandlers(eventKey));
            }
            handlerRetry.execute(() -> handlersExecutionStrategy.processHandlers(handlersCurrent, event));
        } catch (HandlersException e) {
            retryCountStore.incrementRetryCount(eventKey);
            List<H> errorHandlers = getObjectsByTheirNames(e.getHandlerExceptions());
            retryCountStore.setUnworkedHandlers(eventKey, errorHandlers);

            if (retryCountStore.getRetryCount(eventKey) == countRetryableTopic) {
                ack.acknowledge();

                Map<String, String> errorMessages = e.getHandlerExceptions().stream()
                                                     .collect(Collectors.toMap(
                                                             HandlerException::getHandlerName,
                                                             HandlerException::getMessage
                                                     ));

                dlqMessageSender.send(event,
                                      errorMessages.keySet(),
                                      countRetryableTopic,
                                      errorMessages,
                                      eventKey,
                                      topicDlqName
                );

                retryCountStore.clearRetryState(eventKey);
            } else {
                throw e;
            }
        }
    }
}