package faang.school.achievement.client.redis;

import org.springframework.data.redis.listener.ChannelTopic;

public class AchievementTopic extends ChannelTopic {
    public AchievementTopic(String name) {
        super(name);
    }
}
