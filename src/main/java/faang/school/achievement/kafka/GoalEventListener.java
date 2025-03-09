package faang.school.achievement.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalEventListener {

    private final EventHandler<String> collectorAchievementHandler;

    @KafkaListener(
        topics = "${spring.data.kafka.channel.goal-achievement-event}",
        groupId = "${spring.data.kafka.group-id}"
    )
    public void listen(String event) {
        collectorAchievementHandler.handle(event);
    }
}
