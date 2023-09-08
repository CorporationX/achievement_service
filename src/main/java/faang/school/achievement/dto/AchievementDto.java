package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementDto {
    private long receiverId;
    private String title;
    private Rarity rarity;
}
