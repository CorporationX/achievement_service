package faang.school.achievement.kafka;

import faang.school.achievement.dto.TeamEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamEventListener {
  private final List<KafkaEventHandler> eventHandlers;

  @KafkaListener(topics = "team_topic")
  public void listen(TeamEvent event) {
    eventHandlers.forEach(handler -> handler.handle(event));
  }
}