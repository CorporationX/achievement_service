package faang.school.achievement.kafka.listeners;

import faang.school.achievement.kafka.events.Event;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventListener<T extends Event> {
    void handle(ConsumerRecord<String, T> message);
}
