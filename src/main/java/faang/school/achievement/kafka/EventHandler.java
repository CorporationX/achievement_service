package faang.school.achievement.kafka;

public interface EventHandler<T> {

    public void handle(T event);
}
