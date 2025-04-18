package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowEventListener extends AbstractEventListener<FollowerEvent> {

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    public FollowEventListener(ObjectMapper objectMapper, List<EventHandler<FollowerEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(followerChannel);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, FollowerEvent.class);
    }
}
