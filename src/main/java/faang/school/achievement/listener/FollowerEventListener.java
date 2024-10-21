package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.eventHandler.FollowersAchievementHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final FollowersAchievementHandler followersAchievementHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received Follower event: {}", message.toString());
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            followersAchievementHandler.handle(event);
        } catch (IOException e) {
            log.error("An exception was thrown when receiving an follower event");
            throw new RuntimeException(e);
        }
    }

}
