package faang.school.achievement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAchievementDto {
    private long userId;
    private List<Long> achievements;
}
