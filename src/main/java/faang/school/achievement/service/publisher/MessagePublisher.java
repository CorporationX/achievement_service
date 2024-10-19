package faang.school.achievement.service.publisher;

public interface MessagePublisher<T> {

    void publish(T message);
}
