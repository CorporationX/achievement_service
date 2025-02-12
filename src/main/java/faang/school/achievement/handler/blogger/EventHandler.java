package faang.school.achievement.handler.blogger;

public interface EventHandler<T> {
    void handle(T event);
}
