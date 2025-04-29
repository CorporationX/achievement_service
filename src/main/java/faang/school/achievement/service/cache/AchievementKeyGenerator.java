package faang.school.achievement.service.cache;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class AchievementKeyGenerator {

    @Value("${spring.data.redis.key-prefix.achievement}")
    private String keyPrefix;

    public String createAchievementKey(@NotBlank String title) {
        String achievementTitle = title.toLowerCase().replaceAll(" ", "_");
        return keyPrefix + achievementTitle;
    }
}
