package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannelName;

    public CommentEventListener(List<EventHandler<CommentEvent>> eventHandlers, ObjectMapper objectMapper) {
        super(eventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, CommentEvent.class);
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(commentChannelName);
    }
}
