package faang.school.achievement.infrastructure.kafka;

import faang.school.achievement.exception.HandlersException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HandlerRetry {
    @Retryable(retryFor = {HandlersException.class},
               maxAttemptsExpression = "${app.kafka.handler.retry.max-attempts:3}",
               backoff = @Backoff(delayExpression = "${app.kafka.handler.retry.initial-delay-ms:1000}",
                                  multiplierExpression = "${app.kafka.handler.retry.multiplier:2.0}",
                                  maxDelayExpression = "${app.kafka.handler.retry.max-delay-ms:10000}"))
    public void execute(Runnable fn) {

        try {
            fn.run();
        } catch (HandlersException e) {
            log.error("Retry");
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, Runnable fn) {
        if (e instanceof HandlersException) {
            throw (HandlersException) e;
        } else {
            throw new HandlersException("Processing handlers failed with an error", List.of());
        }
    }
}