package faang.school.achievement.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils {
    public static final String ERROR_DESERIALIZING = "Error deserializing ";
    public static final String SERIALIZATION_ERROR = "Serialization error";

    private final ObjectMapper objectMapper;

    public <T> T deserialize(String jsonResponse, Class<T> classType) {
        try {
            return objectMapper.readValue(jsonResponse, classType);
        } catch (JsonProcessingException e) {
            log.error(ERROR_DESERIALIZING + classType.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }

    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(SERIALIZATION_ERROR, e);
            throw new RuntimeException(e);
        }
    }
}