package faang.school.achievement.service.event_handler;

public interface EventHandler<T> {

    void handleEvent(T event);
}
