package faang.school.achievement.handler.mentorship;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.listener.MentorshipEventDto;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.UserAchievementService;
import faang.school.achievement.service.cache.AchievementCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class MentorshipEventHandler implements EventHandler<MentorshipEventDto> {

    protected final AchievementProgressService achievementProgressService;
    protected final AchievementCacheService achievementCacheService;
    protected final UserAchievementService userAchievementService;

    @Async(value = "achievementPool")
    @Transactional
    public void handleEvent(MentorshipEventDto eventDto) {
        AchievementDto achievementDto = achievementCacheService.getAchievement(getAchievementTitle());

        if (userAchievementService.hasAchievement(eventDto.getMentorId(), achievementDto.getId())) {
            log.info("The user {} has an achievementDto {}", eventDto.getMentorId(), achievementDto.getId());
            return;
        }

        achievementProgressService.createProgressIfNecessary(eventDto.getMentorId(), achievementDto.getId());

        AchievementProgress achievementProgress = achievementProgressService
                .getProgress(eventDto.getMentorId(), achievementDto.getId());

        AchievementProgress progress = achievementProgressService.progressIncrement(achievementProgress.getId());

        if (progress.getCurrentPoints() >= achievementDto.getPoints()) {
            userAchievementService.giveAchievement(eventDto.getMentorId(), achievementDto.getId());
        }
    }

    protected abstract String getAchievementTitle();
}
