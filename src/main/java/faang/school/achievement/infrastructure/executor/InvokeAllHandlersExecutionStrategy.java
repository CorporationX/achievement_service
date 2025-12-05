package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.TimedEventHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static faang.school.achievement.utils.Utils.extractRootCauseMessage;
import static faang.school.achievement.utils.Utils.getSimpleClassName;

@Component("invokeAllHandlersExecutionStrategy")
@RequiredArgsConstructor
public class InvokeAllHandlersExecutionStrategy<T> implements HandlersExecutionStrategy<T, TimedEventHandler<T>> {
    @Qualifier("handlersExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;
    @Value("${app.kafka.handler.retry.max-delay-ms:20000}")
    private long handlerDefaultCompletionTime;

    @Override
    public void processHandlers(@NonNull List<TimedEventHandler<T>> handlers, @NonNull T event) {
        ExecutorService executor = taskExecutor.getThreadPoolExecutor();
        List<Callable<Void>> tasks = handlers.stream().map(handler -> (Callable<Void>) () -> {
            try {
                handler.handle(event);
            } catch (HandlerException e) {
                throw e;
            } catch (Exception e) {
                String name = getSimpleClassName(handler);
                throw new HandlerException(name, extractRootCauseMessage(e), e);
            }
            return null;
        }).toList();

        List<Future<Void>> futures;
        long handlerCompletionTime = handlers.stream().map(TimedEventHandler::getHandlerExecutionTime)
            .max(Long::compare).orElse(handlerDefaultCompletionTime);
        try {
            futures = executor.invokeAll(tasks, handlerCompletionTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            HandlerException he = new HandlerException("ALL_HANDLERS", "Handlers processing was interrupted", e);
            throw new HandlersException("Handlers execution interrupted", List.of(he));
        }

        List<HandlerException> failures = new ArrayList<>();

        for (int i = 0; i < handlers.size(); i++) {
            TimedEventHandler<T> handler = handlers.get(i);
            Future<Void> future = futures.get(i);
            String handlerName = handler.getClass().getSimpleName();

            if (future.isCancelled()) {
                failures.add(new HandlerException(handlerName, "Handler execution timeout"));
                continue;
            }

            try {
                future.get();
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof HandlerException he) {
                    failures.add(he);
                } else {
                    failures.add(new HandlerException(handlerName, "Handler failed with unexpected error", cause));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failures.add(new HandlerException(handlerName, "Handler thread interrupted", e));
            }
        }

        if (!failures.isEmpty()) {
            throw new HandlersException("Some handlers failed", failures);
        }
    }
}