package faang.school.achievement.listener;

import faang.school.achievement.achievement_handler.AbstractEventHandler;
import faang.school.achievement.dto.event.RecommendationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener {
    private final List<AbstractEventHandler<RecommendationEvent>> recommendationEventHandlers;

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${kafka.group}")
    public void consume(RecommendationEvent event) {
        recommendationEventHandlers.forEach(a -> a.proceedAchievement(event.receiverId()));
    }
}
