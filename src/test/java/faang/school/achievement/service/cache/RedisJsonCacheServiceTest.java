package faang.school.achievement.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisConnectionException;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisJsonCacheServiceTest {

    public static final String MAP_NAME = "testMap";
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisJsonCacheService<TestValue> cacheService;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    private final String key = "testKey";
    private final TestValue testValue = new TestValue("testValue");

    @Test
    void put_shouldStoreValueInRedis() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheService.put(key, testValue);

        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(key, testValue);
    }

    @Test
    void put_withExpiry_shouldStoreValueInRedisWithExpiry() {
        Duration duration = Duration.ofMinutes(10);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheService.put(key, testValue, duration);

        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(key, testValue, duration);
    }

    @Test
    void get_shouldReturnValueWhenPresent() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(testValue);
        when(objectMapper.convertValue(testValue, TestValue.class)).thenReturn(testValue);

        TestValue result = cacheService.get(key, TestValue.class);

        assertEquals(testValue, result);
    }

    @Test
    void get_shouldReturnNullWhenNotPresent() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        TestValue result = cacheService.get(key, TestValue.class);

        assertNull(result);
    }

    @Test
    void exists_shouldReturnTrueWhenKeyExists() {
        when(redisTemplate.hasKey(key)).thenReturn(true);

        boolean result = cacheService.exists(key);

        assertTrue(result);
    }

    @Test
    void exists_shouldThrowExceptionWhenRedisConnectionFails() {
        String message = "Redis connection failed";
        when(redisTemplate.hasKey(key)).thenReturn(null);

        RedisConnectionException exception = assertThrows(RedisConnectionException.class, () -> cacheService.exists(key));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testPut_ShouldPutValueInRedisHash() {
        String hashKey = "testKey";
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        cacheService.put(MAP_NAME, hashKey, testValue);

        verify(hashOperations).put(MAP_NAME, hashKey, testValue);
    }

    @Test
    void testGetFromMap_ShouldReturnDeserializedObject() {
        String hashKey = "Hash key";
        TestValue expectedObject = new TestValue("Test Object");

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(MAP_NAME, hashKey)).thenReturn(expectedObject);
        when(objectMapper.convertValue(expectedObject, TestValue.class)).thenReturn(expectedObject);

        TestValue result = cacheService.getFromMap(MAP_NAME, hashKey, TestValue.class);

        assertEquals(expectedObject, result);
        verify(hashOperations).get(MAP_NAME, hashKey);
    }

    @Test
    void testGetValuesFromMap_ShouldReturnDeserializedObjectList() {
        TestValue object1 = new TestValue("Test Object 1");
        TestValue object2 = new TestValue("Test Object 2");
        List<Object> objectList = List.of(object1, object2);
        List<TestValue> expectedList = List.of(object1, object2);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.values(MAP_NAME)).thenReturn(objectList);
        when(objectMapper.convertValue(object1, TestValue.class)).thenReturn(object1);
        when(objectMapper.convertValue(object2, TestValue.class)).thenReturn(object2);

        List<TestValue> result = cacheService.getValuesFromMap(MAP_NAME, TestValue.class);

        verify(hashOperations).values(MAP_NAME);
        verify(objectMapper).convertValue(object1, TestValue.class);
        verify(objectMapper).convertValue(object2, TestValue.class);
        assertEquals(expectedList, result);
    }

    @Data
    private static class TestValue {
        private String value;

        public TestValue(String value) {
            this.value = value;
        }
    }
}
