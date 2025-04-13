package faang.school.achievement.service;

public interface EventHandler<T> {
    public void handle(T event);
}
