package faang.school.achievement.publisher.redis;

import faang.school.achievement.model.AchievementEvent;

public interface MessagePublisher {

    void publishMessage(AchievementEvent achievementEvent);
}
