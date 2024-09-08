package faang.school.achievement.handler.managerAchievement;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManagerAchievementHandler extends TeamEventHandler {
    private final String achievementsTitle;

    public ManagerAchievementHandler(
            AchievementCache achievementCache,
            AchievementService achievementService,
            AchievementProgressRepository achievementProgressRepository,
            AchievementMapper mapper,
            @Value("${data.achievements.titles.manager}") String achievementsTitle) {
        super(achievementCache, achievementService, achievementProgressRepository, mapper);
        this.achievementsTitle = achievementsTitle;
    }
    @Transactional
    @Async
    public void process(TeamEvent teamEvent) {
        processTeamEvent(teamEvent, achievementsTitle);
    }
}