package faang.school.achievement.infrastructure.store;

import faang.school.achievement.handlers.EventHandler;

import java.util.List;

public interface RetryCountStore<T, H extends EventHandler<T>> {
    void incrementRetryCount(String eventKey);

    int getRetryCount(String eventKey);

    void clearRetryState(String eventKey);

    void setUnworkedHandlers(String eventKey, List<H> unworkedHandlers);

    List<H> getUnworkedHandlers(String eventKey);
}