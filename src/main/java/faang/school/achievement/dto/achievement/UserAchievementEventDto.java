package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserAchievementEventDto {

    private final long userId;
    private final long achievementId;
    private final Rarity rarity;
    private final String title;

    private final LocalDateTime createdAt;

}
