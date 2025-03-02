package faang.school.achievement.redis;

public interface MessagePublisher {
    void publish(Object message, String topic);
}
