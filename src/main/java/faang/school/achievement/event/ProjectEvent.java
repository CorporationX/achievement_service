package faang.school.achievement.event;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectEvent {
    @NotNull
    private Long userId;

    @NotNull
    private Long projectId;
}
