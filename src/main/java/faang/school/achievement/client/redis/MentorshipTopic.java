package faang.school.achievement.client.redis;

import org.springframework.data.redis.listener.ChannelTopic;

public class MentorshipTopic extends ChannelTopic {
    public MentorshipTopic(String name) {
        super(name);
    }
}
