package faang.school.achievement.config.infrastructure;

import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handlers.TimedEventHandler;
import faang.school.achievement.infrastructure.event.EventProcessingService;
import faang.school.achievement.infrastructure.event.RetryableEventProcessingService;
import faang.school.achievement.infrastructure.executor.HandlersExecutionStrategy;
import faang.school.achievement.infrastructure.kafka.HandlerRetry;
import faang.school.achievement.infrastructure.kafka.dlq.DlqMessageSender;
import faang.school.achievement.infrastructure.store.RetryCountStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventProcessingConfig {

    @Bean
    public EventProcessingService<CommentEventDto, TimedEventHandler<CommentEventDto>> commentEventProcessingService(
        List<TimedEventHandler<CommentEventDto>> handlers,
        HandlerRetry handlerRetry,
        @Qualifier("InMemoryRetryCountStore")
        RetryCountStore<CommentEventDto, TimedEventHandler<CommentEventDto>> retryCountStore,
        @Qualifier("invokeAllHandlersExecutionStrategy")
        HandlersExecutionStrategy<CommentEventDto, TimedEventHandler<CommentEventDto>> handlersExecutionStrategy,
        DlqMessageSender dlqMessageSender,
        @Value("${app.kafka.retryable-topic.retry:3}")
        long maxRetryCount,
        @Value("${app.kafka.topics.comment-create-topic-dlq:comment-create-topic-dlq}")
        String dlqTopicName) {
        return new RetryableEventProcessingService<>(
            handlers,
            handlerRetry,
            retryCountStore,
            handlersExecutionStrategy,
            dlqMessageSender,
            maxRetryCount,
            dlqTopicName
        );
    }
}