package faang.school.achievement.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicEvent {
    private Long id;
    private Long userId;

    private String title;
    private String description;
    private String profilePicLink;
}
