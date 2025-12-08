package faang.school.achievement.infrastructure.store;

import faang.school.achievement.handlers.TimedEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component("InMemoryRetryCountStore")
public class InMemoryRetryCountStore<T> implements RetryCountStore<T, TimedEventHandler<T>> {

    private final ConcurrentMap<String, AtomicInteger> retryCountMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<TimedEventHandler<T>>> unworkedHandlers = new ConcurrentHashMap<>();

    @Override
    public void incrementRetryCount(String eventKey) {
        retryCountMap.computeIfAbsent(eventKey, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public void setUnworkedHandlers(String eventKey, List<TimedEventHandler<T>> unworkedHandlers) {
        this.unworkedHandlers.put(eventKey, unworkedHandlers);
    }

    @Override
    public List<TimedEventHandler<T>> getUnworkedHandlers(String eventKey) {
        return this.unworkedHandlers.get(eventKey);
    }

    @Override
    public int getRetryCount(String eventKey) {
        AtomicInteger counter = retryCountMap.get(eventKey);
        return counter != null
                ? counter.get()
                : 0;
    }

    @Override
    public void clearRetryState(String eventKey) {
        retryCountMap.remove(eventKey);
        unworkedHandlers.remove(eventKey);
    }
}