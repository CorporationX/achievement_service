package faang.school.achievement.messaging.publishers;

import faang.school.achievement.model.UserAchievement;

public interface MessagePublisher {
    void publish(UserAchievement achievement);
}
