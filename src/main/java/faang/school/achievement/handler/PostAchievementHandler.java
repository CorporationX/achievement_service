package faang.school.achievement.handler;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.model.EventType;
import faang.school.achievement.propertie.RedisConnectionProperties;
import faang.school.achievement.service.AchievementService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class PostAchievementHandler extends AchievementHandler {
    private final String channel;

    public PostAchievementHandler(AchievementService achievementService,
                                  RedisConnectionProperties redisConnectionProperties) {
        super(achievementService, EventType.PUBLISHED_POST);
        this.channel = redisConnectionProperties.getTopic(RedisConnectionProperties.TopicKey.POST);
        log.info("Initialized post achievement handler");
    }

    @Override
    @Async("postEventPool")
    public void handleEvent(EventDto event) {
        super.handleEvent(event);
    }
}
