package faang.school.achievement.thread_pool;

import faang.school.achievement.model.EventType;
import faang.school.achievement.properties.ThreadPoolSizeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventThreadPools {
    private final ThreadPoolSizeProperties threadPoolSizeProperties;

    @Bean
    private Map<EventType, ExecutorService> eventPools() {
        return Map.of(
                EventType.PUBLISHED_POST, Executors.newFixedThreadPool(threadPoolSizeProperties.getPublishedPost())
        );
    }

    public ExecutorService getThreadPoolFor(EventType eventType) {
        ExecutorService eventThreadPool = eventPools().get(eventType);
        if (eventThreadPool == null) {
            throw new RuntimeException("the thread pool has not been created for " + eventType);
        }
        return eventThreadPool;
    }
}
