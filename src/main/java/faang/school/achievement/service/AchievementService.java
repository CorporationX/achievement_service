package faang.school.achievement.service;

import faang.school.achievement.dto.event.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.publisher.EventPublisher;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final EventPublisher<AchievementEvent> eventPublisher;

    // Just a test method for event publishing. You can delete it freely.
    public void giveAchievement(Long userId, String achievementTitle) {
        Achievement achievement = achievementRepository.findByTitle(achievementTitle).orElseThrow();
        AchievementEvent event = AchievementEvent.builder()
                .userId(userId)
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .build();
        eventPublisher.publish(event);
    }
}
