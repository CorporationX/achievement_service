package faang.school.achievement.kafka;

public interface EventHandler<T> {

    public void collectEvent(T event);
}
