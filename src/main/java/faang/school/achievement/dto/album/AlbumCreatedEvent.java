package faang.school.achievement.dto.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreatedEvent {

    private long id;

    private String title;

    private String description;

}
