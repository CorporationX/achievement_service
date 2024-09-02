package faang.school.achievement.event;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectEventDto implements EventDto{
    private long authorId;
    private long projectId;
}
