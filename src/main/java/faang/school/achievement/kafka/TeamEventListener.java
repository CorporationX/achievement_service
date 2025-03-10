package faang.school.achievement.kafka;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.handler.kafka.KafkaEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamEventListener {
  private final List<KafkaEventHandler> eventHandlers;

  @KafkaListener(topics = "${achievement-service.kafka.team-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void listen(TeamEvent event) {
    eventHandlers.forEach(handler -> handler.handle(event));
  }
}