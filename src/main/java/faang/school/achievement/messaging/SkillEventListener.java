package faang.school.achievement.messaging;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class SkillEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
