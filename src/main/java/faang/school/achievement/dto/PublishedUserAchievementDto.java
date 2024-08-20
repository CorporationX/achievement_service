package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishedUserAchievementDto {
    private Long id;
    private Long achievementId;
    private Long userId;
    private String title;
}
