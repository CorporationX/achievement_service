package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class RedisMessageListener<T> {
    protected final ObjectMapper objectMapper;

    protected T handleEvent(Class<T> tClass, Message message) {
        try {
            return objectMapper.readValue(message.getBody(), tClass);
        } catch (IOException e) {
            log.error("Failed to deserialize event",e);
            throw new RuntimeException("Failed to deserialize event");
        }
    }
}
