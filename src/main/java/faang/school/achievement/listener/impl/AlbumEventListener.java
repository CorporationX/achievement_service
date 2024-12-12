package faang.school.achievement.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumEventListener extends AbstractEventListener<AlbumCreatedEvent> {

    @Value("${spring.data.redis.channel.album}")
    private String channelName;

    public AlbumEventListener(List<EventHandler<AlbumCreatedEvent>> eventHandlers, ObjectMapper objectMapper) {
        super(eventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, AlbumCreatedEvent.class);
    }

    public ChannelTopic getTopic() {
        return new ChannelTopic(channelName);
    }
}