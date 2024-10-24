package faang.school.achievement.config.achievent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "achievements")
public class AchievementConfiguration {

    private AchievementProp handsome;
    private AchievementProp evilCommenter;

    @Getter
    @Setter
    public static class AchievementProp {
        private String title;
        private int pointsToAchieve;
    }
}
