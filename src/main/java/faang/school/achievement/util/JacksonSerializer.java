package faang.school.achievement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.AchievementEventDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JacksonSerializer implements Serializer<AchievementEventDto> {
    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String topic, AchievementEventDto data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }
}
