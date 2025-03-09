package faang.school.achievement.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoalSetEvent {

    private Long userId;
    private Long goalId;
}