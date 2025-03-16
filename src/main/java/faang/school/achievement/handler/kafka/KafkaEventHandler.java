package faang.school.achievement.handler.kafka;

public interface KafkaEventHandler<T> {
  void handle(T event);
}
