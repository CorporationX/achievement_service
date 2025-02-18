package faang.school.achievement.kafka.listenr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.handler.LibrarianAchievementHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumEventListener {

    private final LibrarianAchievementHandler handler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.album.created.topic}", groupId = "achievement-group")
    public void listen(String input) {
        AlbumCreatedEvent event = mapInputToAlbumCreatedEvent(input);
        handler.applyAchievement(event);
    }

    private AlbumCreatedEvent mapInputToAlbumCreatedEvent(String input) {
        AlbumCreatedEvent event;
        try {
            event = objectMapper.readValue(input, AlbumCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}
