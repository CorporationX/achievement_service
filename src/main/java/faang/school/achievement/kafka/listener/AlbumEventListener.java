package faang.school.achievement.kafka.listener;

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

    @KafkaListener(topics = "${kafka.album.created.topic}", groupId = "${spring.kafka.group.id}")
    public void listen(String message) {
        AlbumCreatedEvent event = mapInputToAlbumCreatedEvent(message);
        handler.applyAchievement(event);
    }

    private AlbumCreatedEvent mapInputToAlbumCreatedEvent(String input) {
        try {
            return objectMapper.readValue(input, AlbumCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
