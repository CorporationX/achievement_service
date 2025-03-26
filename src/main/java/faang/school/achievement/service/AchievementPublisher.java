package faang.school.achievement.service;

import faang.school.achievement.event.AchievementEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class AchievementPublisher extends Publisher<AchievementEvent> {
    private final ChannelTopic topic;

    public AchievementPublisher(@Qualifier("achievementTopic") ChannelTopic topic) {
        this.topic = topic;
    }

    @Override
    ChannelTopic topic() {
        return topic;
    }
}
