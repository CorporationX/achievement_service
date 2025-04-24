package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowEventListener extends AbstractEventListener<FollowerEvent> {

    private final List<EventHandler<FollowerEvent>> eventHandlers;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    public FollowEventListener(ObjectMapper objectMapper, List<EventHandler<FollowerEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
        this.eventHandlers = eventHandlers;
    }

    @PostConstruct
    public void init() {
        if (eventHandlers.isEmpty()) {
            log.warn("No event handlers registered for FollowEventListener. Events will be ignored.");
        }
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
