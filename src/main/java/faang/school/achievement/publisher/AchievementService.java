package faang.school.achievement.publisher;

import faang.school.achievement.config.context.AchievementEventPublisher;
import faang.school.achievement.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementEventPublisher publisher;

    public void awardAchievement(Long userId, String title) {

        publisher.publish(
                AchievementEvent.builder()
                        .userId(userId)
                        .achievementTitle(title)
                        .receivedAt(LocalDateTime.now())
                        .build()
        );
    }
}
