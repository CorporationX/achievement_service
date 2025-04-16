package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.listener.MentorshipEventDto;
import faang.school.achievement.exception.MentorshipEventDeserializationException;
import faang.school.achievement.handler.mentorship.MentorshipEventHandler;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MentorshipEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    private final List<MentorshipEventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipEventDto mentorshipEventDto;

        try {
            mentorshipEventDto = objectMapper.readValue(message.getBody(), MentorshipEventDto.class);
        } catch (IOException e) {
            throw new MentorshipEventDeserializationException("It was not possible to deserialization the object", e);
        }
        //TODO валидация
        handlers.forEach(handler -> handler.handleEvent(mentorshipEventDto));
    }
}