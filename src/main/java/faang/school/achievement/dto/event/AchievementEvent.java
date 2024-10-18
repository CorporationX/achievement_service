package faang.school.achievement.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@Builder
public class AchievementEvent implements Serializable {
    private String title;
    private long userId;
}
