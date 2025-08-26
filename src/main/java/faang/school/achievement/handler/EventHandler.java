package faang.school.achievement.handler;

public interface EventHandler<T> {
    Class<?> getInstance();

    void handle(T event);
}
