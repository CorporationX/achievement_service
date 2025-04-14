package faang.school.achievement.thread_pool;

import faang.school.achievement.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class EventThreadPools {
    private final Map<EventType, ExecutorService> eventPools = Map.of(
            EventType.PUBLISHED_POST, Executors.newFixedThreadPool(10)
    );

    public ExecutorService getThreadPoolFor(EventType eventType) {
        ExecutorService eventThreadPool = eventPools.get(eventType);
        if (eventThreadPool == null) {
            throw new RuntimeException("the thread pool has not been created for " + eventType);
        }
        return eventThreadPool;
    }
}
