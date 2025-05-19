package faang.school.achievement.handler;

import faang.school.achievement.dto.event.CommentEvent;
import org.springframework.scheduling.annotation.Async;

public interface EventHandler<T> {

    @Async("taskExecutor")
    void handle(T event);
}
