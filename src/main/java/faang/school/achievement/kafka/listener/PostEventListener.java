package faang.school.achievement.kafka.listener;

import faang.school.achievement.achievement_handler.OpinionLeaderAchievementHandler;
import faang.school.achievement.dto.PostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final OpinionLeaderAchievementHandler opinionLeaderAchievementHandler;

    @KafkaListener(
            topicPartitions = @TopicPartition(
                    topic = "${spring.kafka.topics.achievement}",
                    partitions = "${spring.kafka.partitions.for_post_event}"
            ),
            containerFactory = "postEventContainerFactory"

    )
    public void listener(PostEvent postEvent) {
        opinionLeaderAchievementHandler.proceedAchievement(postEvent.receiverId());
    }
}
