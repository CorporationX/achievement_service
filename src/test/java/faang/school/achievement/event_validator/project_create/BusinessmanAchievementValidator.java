package faang.school.achievement.event_validator.project_create;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
public class BusinessmanAchievementValidator implements ProjectCreateEventValidator {
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private AchievementProgressRepository achievementProgressRepository;

    @Override
    public boolean isValid(long userId) {
        String achievementTitle = "BUSINESSMAN";
        UserAchievement userAchievementFromDatabase = userAchievementRepository.findByUserId(userId)
                .stream()
                .filter(userAchievement -> userAchievement.getAchievement().getTitle().equals(achievementTitle))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        AchievementProgress achievementProgress = achievementProgressRepository
                .findByUserIdAndAchievementId(userId, userAchievementFromDatabase.getAchievement().getId())
                .orElseThrow(NoSuchElementException::new);

        return userAchievementFromDatabase.getAchievement().getTitle().equals(achievementTitle)
                && achievementProgress.getCurrentPoints() >= userAchievementFromDatabase.getAchievement().getPoints();
    }
}
