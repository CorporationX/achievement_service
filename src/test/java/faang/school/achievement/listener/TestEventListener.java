package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.handler.TestEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public class TestEventListener extends AbstractEventListener<TestEvent> {

    public TestEventListener(List<EventHandler<TestEvent>> eventHandlers, ObjectMapper objectMapper) {
        super(eventHandlers, objectMapper);
    }

    @Override
    public ChannelTopic getTopic() {
        return null;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
