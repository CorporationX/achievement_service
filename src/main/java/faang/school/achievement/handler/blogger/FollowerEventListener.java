package faang.school.achievement.handler.blogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final BloggerAchievementHandler bloggerAchievementHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            FollowerEvent event = objectMapper.readValue(json, FollowerEvent.class);
            bloggerAchievementHandler.handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
