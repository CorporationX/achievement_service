package faang.school.achievement.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "thread-pool-sizes.events")
public class ThreadPoolSizeProperties {
    private int publishedPost;
}
