package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserAchievementEventDto {
    @Positive
    private final long userId;

    @Positive
    private final long achievementId;

    @NotNull
    private final Rarity rarity;

    @NotBlank
    private final String title;

    private final LocalDateTime createdAt;

}
