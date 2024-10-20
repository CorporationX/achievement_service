package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.PostEventDto;
import faang.school.achievement.handler.WriterAchievementHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final WriterAchievementHandler writerAchievementHandle;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostEventDto postEventDto = objectMapper.readValue(message.getBody(), PostEventDto.class);
            log.info("Get message {}:", postEventDto);
            writerAchievementHandle.computeAchievement(postEventDto);

        } catch (IOException exception) {
            log.error("Error with mapping to PostEventDto");
            throw new IllegalArgumentException("Error with mapping to PostEventDto");
        }

    }
}
