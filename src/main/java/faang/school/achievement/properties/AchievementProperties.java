package faang.school.achievement.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "achievement-service")
@Getter
@Setter
public class AchievementProperties {
    private Redis redis;

    @Getter
    @Setter
    public static class Redis {
        private String profilePicChannel;
    }
}
