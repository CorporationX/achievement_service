package faang.school.achievement.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamEvent {

    private Long authorId;
    private Long projectId;
    private Long teamId;
}
