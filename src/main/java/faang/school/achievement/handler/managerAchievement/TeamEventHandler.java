package faang.school.achievement.handler.managerAchievement;

import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TeamEventHandler implements EventHandler<TeamEvent> {
    private final AchievementCache achievementCache;
    private final AchievementService service;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper mapper;

    protected void processTeamEvent(TeamEvent event, String title) {
        Long userId = event.getUserId();
        Achievement achievement = achievementCache.get(title);
        if (service.hasAchievement(userId, achievement.getId())) {
            return;
        }
        service.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgressDto progressDto = service.getProgress(userId, achievement.getId());
        AchievementProgress progress = mapper.toAchievementProgress(progressDto);
        progress.increment();
        achievementProgressRepository.save(progress);
        if (achievement.getPoints() == progress.getCurrentPoints()) {
            service.giveAchievement(userId, achievement);
        }
    }
}