package faang.school.achievement.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.handler.EvilCommenterAchievementHandler;
import faang.school.achievement.kafka.EventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements EventListener {

    private final EvilCommenterAchievementHandler handler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "${spring.kafka.group.id}")
    @Override
    public void listen(String input) {
        CommentEvent event = mapInputToCommentEvent(input);
        handler.applyAchievement(event);
    }

    private CommentEvent mapInputToCommentEvent(String input) {
        try {
            return objectMapper.readValue(input, CommentEvent.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException();
        }
    }
}
