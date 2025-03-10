package faang.school.achievement.handler.kafka;

import faang.school.achievement.dto.TeamEvent;

public interface KafkaEventHandler {
  void handle(TeamEvent event);
}