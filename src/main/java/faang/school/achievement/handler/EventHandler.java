package faang.school.achievement.handler;

import org.springframework.scheduling.annotation.Async;

public interface EventHandler<T> {

    @Async("executorService")
    void handle(Long userId);
}