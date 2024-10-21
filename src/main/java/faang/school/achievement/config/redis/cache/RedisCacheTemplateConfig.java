package faang.school.achievement.config.redis.cache;

import faang.school.achievement.dto.AchievementDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheTemplateConfig {
    @Bean
    public Jackson2JsonRedisSerializer<AchievementDto> achievementRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(AchievementDto.class);
    }

    @Bean
    public RedisTemplate<String, AchievementDto> achievementRedisTemplate(JedisConnectionFactory jedisConnectionFactory,
                                                                          StringRedisSerializer stringRedisSerializer,
                                                                          Jackson2JsonRedisSerializer<AchievementDto> achievementRedisSerializer) {
        RedisTemplate<String, AchievementDto> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(achievementRedisSerializer);

        return template;
    }
}
