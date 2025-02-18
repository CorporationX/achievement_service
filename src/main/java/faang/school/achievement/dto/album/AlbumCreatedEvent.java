package faang.school.achievement.dto.album;

import faang.school.achievement.enums.AchievementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreatedEvent {

    private long userId;

    private AchievementType title;

}
