package faang.school.achievement.handler;

public interface EventHandler<E> {
    void handle(E event);
}
