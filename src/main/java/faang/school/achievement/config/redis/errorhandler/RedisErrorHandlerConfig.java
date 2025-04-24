package faang.school.achievement.config.redis.errorhandler;

import faang.school.achievement.exception.RedisMessageProcessingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
public class RedisErrorHandlerConfig {
    @Bean
    public ErrorHandler redisErrorHandler() {
        return throwable -> {
            throw new RedisMessageProcessingException(
                    String.format("Redis message processing failed: %s", throwable.getMessage()), throwable);
        };
    }
}
