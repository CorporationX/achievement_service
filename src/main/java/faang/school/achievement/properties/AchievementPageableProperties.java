package faang.school.achievement.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.achievement.pagination")
public class AchievementPageableProperties {

    @NotNull
    private Integer defaultPage;

    @NotNull
    private Integer defaultSize;
}
