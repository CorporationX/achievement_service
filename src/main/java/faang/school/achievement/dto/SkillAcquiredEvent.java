package faang.school.achievement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillAcquiredEvent {

    @NotNull
    @Positive
    private Long authorId;

    @NotNull
    @Positive
    private Long recipientId;

    @NotNull
    @Positive
    private Long skillId;
}
