package faang.school.achievement.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupCacheLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final AchievementCache achievementCache;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        achievementCache.init();
    }
}