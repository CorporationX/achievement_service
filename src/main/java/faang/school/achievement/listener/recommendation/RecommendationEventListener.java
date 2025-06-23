package faang.school.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.events.RecommendationEvent;
import faang.school.achievement.handler.NiceGuyAchievementHandler;
import faang.school.achievement.listener.AbstractEventListener;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("recommendation-event");
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, redisProperties);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            Method handleEventMethod = NiceGuyAchievementHandler.class.getMethod(
                    "handleEvent",
                    RecommendationEvent.class);

            handlers.forEach(handler -> handler.invoke(
                    NiceGuyAchievementHandler.class,
                    handleEventMethod,
                    new Object[]{recommendationEvent}));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
