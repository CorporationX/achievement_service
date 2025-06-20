package faang.school.achievement.config.kafka;

import faang.school.achievement.kafka.events.CommentAddedEvent;
import faang.school.achievement.kafka.events.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final KafkaConsumerSettings kafkaProperties;

    @Value("${spring.kafka.consumers.comment-added.name}")
    private String commentAddedSettingsKey;

    @Value("${spring.kafka.consumers.goal-completed.name}")
    private String goalCompletedSettingsKey;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CommentAddedEvent>
    commentAddedEventKafkaListenerContainerFactory() {
        return buildFactory(CommentAddedEvent.class, commentAddedSettingsKey);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GoalCompletedEvent>
    goalCompletedEventKafkaListenerContainerFactory() {
        return buildFactory(GoalCompletedEvent.class, goalCompletedSettingsKey);
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> buildFactory(
            Class<T> valueType,
            String consumerKey
    ) {
        var settings = kafkaProperties.getConsumers().get(consumerKey);
        if (settings == null) {
            throw new IllegalArgumentException("No settings for key: " + consumerKey);
        }

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, settings.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.getOffsetResetConfig());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, settings.getMaxPollRecords());

        ConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<>(props);

        var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(settings.getConcurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.valueOf(settings.getAckMode()));

        return factory;
    }
}
