package faang.school.achievement.service;

public interface EventHandler<T> {
    void handle(T event);
}
