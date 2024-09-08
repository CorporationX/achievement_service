package faang.school.achievement.handler.opinionLeader;

import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PostEventHandler implements EventHandler<PostEvent> {
    private final AchievementCache postEventCache;
    private final AchievementService service;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper mapper;

    protected void processPostEvent(PostEvent event, String title) {
        Long userId = event.getPostId();
        Achievement newAchievement = postEventCache.get(title);
        if (service.hasAchievement(userId, newAchievement.getId())) {
            return;
        }
        service.createProgressIfNecessary(userId, newAchievement.getId());
        AchievementProgressDto progressDto = service.getProgress(userId, newAchievement.getId());
        AchievementProgress progress = mapper.toAchievementProgress(progressDto);
        progress.increment();
        achievementProgressRepository.save(progress);
        if (newAchievement.getPoints() == progress.getCurrentPoints()) {
            service.giveAchievement(userId, newAchievement);
        }
    }
}
