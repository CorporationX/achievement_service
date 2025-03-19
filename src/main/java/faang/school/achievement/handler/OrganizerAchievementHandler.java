package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class OrganizerAchievementHandler extends EventHandler<InviteSentEvent>{
    @Value("${achievement-titles.organizer}")
    private String organizerTitle;

    public OrganizerAchievementHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    @Async("fixedThreadPool")
    @Transactional
    public void handle(InviteSentEvent event) {
        var achievement = achievementCache.get(organizerTitle);
        if (achievement == null) {
            log.warn("Достижение '{}' не найдено в кэше", organizerTitle);
            return;
        }

        long inviterId = event.getInviterId();
        long achievementId = achievement.getId();

        if (achievementService.hasAchievement(inviterId, achievementId)) {
            return;
        }

        achievementService.createProgressIfNecessary(inviterId, achievementId);

        var achievementProgress = achievementService.getProgress(inviterId, achievementId);
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(achievementId, inviterId);
            log.info("Пользователь {} получил достижение '{}'", inviterId, organizerTitle);
        }

        achievementService.saveProgress(achievementProgress);
    }
}
