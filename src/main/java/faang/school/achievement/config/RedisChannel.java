package faang.school.achievement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis.channel")
@Getter
@Setter
public class RedisChannel {

    private String achievement;
    private String follower;
}
