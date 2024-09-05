package faang.school.achievement.event.goal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GoalSetEvent {
    @NotNull
    private Long userId;

    @NotNull
    private Long goalId;
}
