package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.listener.MentorshipEventDto;
import faang.school.achievement.exception.MentorshipEventDeserializationException;
import faang.school.achievement.exception.MentorshipEventValidationException;
import faang.school.achievement.handler.mentorship.MentorshipEventHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<MentorshipEventHandler> handlers;
    private final Validator validator;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received new message: {}", new String(message.getBody()));

        try {
            MentorshipEventDto mentorshipEventDto = objectMapper.readValue(message.getBody(), MentorshipEventDto.class);
            log.debug("Deserialized event: {}", mentorshipEventDto);

            Set<ConstraintViolation<MentorshipEventDto>> violations = validator.validate(mentorshipEventDto);
            if (!violations.isEmpty()) {
                log.error("Validation failed for MentorshipEventDto: {}", violations);
                throw new MentorshipEventValidationException("Validation failed", violations);
            }

            handlers.forEach(handler -> {
                try {
                    log.info("Processing event with handler: {}", handler.getClass().getSimpleName());
                    handler.handleEvent(mentorshipEventDto);
                } catch (Exception e) {
                    log.error("Handler {} failed", handler.getClass().getSimpleName(), e);
                }
            });
        } catch (IOException e) {
            log.error("Deserialization error", e);
            throw new MentorshipEventDeserializationException("Failed to deserialize message", e);
        } catch (MentorshipEventValidationException e) {
            log.error("Validation error", e);
        }
    }
}