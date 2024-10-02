package faang.school.achievement.config.redis;

import faang.school.achievement.listener.redis.RecommendationEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RecommendationRedisConfig {

    @Value("${spring.data.redis.channel.recommendation}")
    private String channelTopic;

    @Bean
    public MessageListenerAdapter recommendationEventListenerAdapter(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(channelTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> recommendationListenerChannelPair(
            @Qualifier("recommendationEventListenerAdapter") MessageListenerAdapter adapter,
            @Qualifier("recommendationTopic") ChannelTopic channelTopic) {
        return Pair.of(adapter, channelTopic);
    }
}
