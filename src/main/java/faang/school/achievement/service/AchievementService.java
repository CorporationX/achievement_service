package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.EventType;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {
    private static final String getting_achievements_log_msg = "getting achievements by event title {}";
    private static final String has_achievement_log_msg = "author with id {} is achieved achievement with id {}? {}";
    private static final String getting_progress_log_msg =
            "getting progress achievement with id {} from the user with id {} and achievement with id {}";
    private static final String save_user_achievement_log_msg =
            "save user achievement with id {} from the user with id {} and achievement with id {}";

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    public List<Achievement> getAchievementByEventType(EventType eventType) {
        List<Achievement> achievements = achievementRepository.findByEvent(eventType);
        log.debug(getting_achievements_log_msg, eventType);
        return achievements;
    }

    public boolean hasUserAchievement(long authorId, long achievementId) {
        boolean isAchieved = userAchievementRepository.existsByUserIdAndAchievementId(authorId, achievementId);
        log.debug(has_achievement_log_msg, authorId, achievementId, isAchieved);
        return isAchieved;
    }

    @Transactional
    public boolean incrementAndCheckAchievementProgress(long authorId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(authorId, achievementId);
        AchievementProgress achievementProgress = achievementProgressRepository.findForUpdate(authorId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("error creating achievement progress"));

        log.debug(getting_progress_log_msg, achievementProgress.getId(), achievementProgress.getUserId(), achievementId);

        achievementProgress.increment();
        return achievementProgress.getAchievement().getGoal() == achievementProgress.getCurrentPoints();
    }

    public void saveAchievementToUser(long authorId, Achievement achievement) {
        UserAchievement userAchievement = userAchievementRepository.save(UserAchievement.builder()
                .userId(authorId)
                .achievement(achievement)
                .build());
        log.debug(save_user_achievement_log_msg, userAchievement.getId(), authorId, achievement.getId());
    }
}