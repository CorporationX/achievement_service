package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("goal_attached");
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, redisProperties);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
