package faang.school.achievement.event;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementEvent {

    private long userId;
    private String achievementTitle;
    private String description;

    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();

    private Rarity rarity;
}
