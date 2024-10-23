package faang.school.achievement.event;

import faang.school.achievement.handler.AchievementHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AchievementEventDispatcher<T> {
    private final List<AchievementHandler<T>> handlers;

    public void dispatchEvent(T event) {
        for (AchievementHandler<T> handler : handlers) {
            handler.handleAchievement(event);
        }
    }
}
