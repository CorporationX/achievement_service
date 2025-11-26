package faang.school.achievement.eventhandler;

public interface EventHandler<T> {
    void handle(T event);

}
