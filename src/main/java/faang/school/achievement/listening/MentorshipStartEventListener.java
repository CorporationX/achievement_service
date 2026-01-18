package faang.school.achievement.listening;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handling.AbstractAchievementHandler;
import faang.school.achievement.model.event.MentorshipStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MentorshipStartEventListener
        extends AbstractMessageListener<MentorshipStartEvent, AbstractAchievementHandler<MentorshipStartEvent>>
        implements MessageListener {

    public MentorshipStartEventListener(
            ObjectMapper objectMapper,
            List<AbstractAchievementHandler<MentorshipStartEvent>> handlers) {
        super(objectMapper, MentorshipStartEvent.class, handlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleMessage(message);
    }
}