package faang.school.achievement.config;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.dto.event.RecommendationEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.host}")
    private String host;

    @Value("${kafka.group}")
    private String groupName;

    @Value("${kafka.concurrency}")
    private Integer concurrency;

    public Map<String, Object> getCommonConsumerProperties() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return config;
    }

    @Bean
    public ConsumerFactory<String, RecommendationEvent> recommendationEventConsumerFactory(){
        Map<String, Object> props = getCommonConsumerProperties();

        JsonDeserializer<RecommendationEvent> deserializer = new JsonDeserializer<>(RecommendationEvent.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationEvent> recommendationEventContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RecommendationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(recommendationEventConsumerFactory());
        factory.setConcurrency(concurrency);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, PostEvent> postEventConsumerFactory(){
        Map<String, Object> props = getCommonConsumerProperties();

        JsonDeserializer<PostEvent> deserializer = new JsonDeserializer<>(PostEvent.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PostEvent> postEventContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, PostEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(postEventConsumerFactory());
        factory.setConcurrency(concurrency);

        return factory;
    }
}
