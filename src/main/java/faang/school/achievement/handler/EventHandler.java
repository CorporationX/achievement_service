package faang.school.achievement.handler;

import org.springframework.stereotype.Component;

@Component
public interface EventHandler<T> {
    Class<?> getInstance();

    void handle(T event);
}
