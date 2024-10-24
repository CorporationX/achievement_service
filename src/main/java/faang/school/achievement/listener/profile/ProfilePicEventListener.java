package faang.school.achievement.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.achievement.profile.ProfilePicEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> {

    public ProfilePicEventListener(Map<Class<?>, List<EventHandler<?>>> handlers,
                                   ObjectMapper objectMapper) {
        super(handlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfilePicEvent.class);
    }
}
