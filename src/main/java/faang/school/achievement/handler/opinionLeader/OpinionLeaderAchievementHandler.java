package faang.school.achievement.handler.opinionLeader;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OpinionLeaderAchievementHandler extends PostEventHandler {
    private final String achievementsTitle;

    public OpinionLeaderAchievementHandler(
            AchievementCache achievementCache,
            AchievementService achievementService,
            AchievementProgressRepository achievementProgressRepository,
            AchievementMapper mapper,
            @Value("${data.achievements.titles.opinion_leader}") String achievementsTitle) {
        super(achievementCache, achievementService, achievementProgressRepository, mapper);
        this.achievementsTitle = achievementsTitle;
    }

    @Transactional
    @Async
    public void process(PostEvent event) {
        processPostEvent(event, achievementsTitle);
    }
}
