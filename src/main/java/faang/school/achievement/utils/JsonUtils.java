package faang.school.achievement.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.JsonDeserializationException;
import faang.school.achievement.exception.JsonSerializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils {
    public static final String ERROR_DESERIALIZING = "Failed to deserialize JSON to %s: %s";
    public static final String SERIALIZATION_ERROR = "Failed to serialize object of type %s: %s";

    private final ObjectMapper objectMapper;

    public <T> T deserialize(String jsonResponse, Class<T> classType) {
        try {
            return objectMapper.readValue(jsonResponse, classType);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format(ERROR_DESERIALIZING, classType.getSimpleName(), e.getMessage());
            log.error(errorMsg, e);
            throw new JsonDeserializationException(errorMsg, e);
        }
    }

    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format(SERIALIZATION_ERROR,
                    object.getClass().getSimpleName(),
                    e.getMessage());
            log.error(errorMsg, e);
            throw new JsonSerializationException(errorMsg, e);
        }
    }
}