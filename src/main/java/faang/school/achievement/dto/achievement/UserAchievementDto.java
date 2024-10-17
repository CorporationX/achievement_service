package faang.school.achievement.dto.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementDto {
    private Long id;
    private Long achievementId;
    private String achievementTitle;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
