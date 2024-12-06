package faang.school.achievement.publisher;

public interface EventPublisher<T> {

    void publish(T event);
}
