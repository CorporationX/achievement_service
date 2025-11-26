package faang.school.achievement.message;

public interface EventHandler<T> {
    void handle(T event);

}
