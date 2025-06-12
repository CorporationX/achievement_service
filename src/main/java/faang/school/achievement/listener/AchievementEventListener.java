package faang.school.achievement.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventListener implements MessageListener {
    // private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@SuppressWarnings("null") Message message, @Nullable byte[] pattern) {
        log.info("Message received from Redis: {}.", message.toString());
    }
}
