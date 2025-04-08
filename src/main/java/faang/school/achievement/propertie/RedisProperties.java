package faang.school.achievement.propertie;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private String channelPost;
    private String channelAchievement;
    private String channelFollower;
}
