package faang.school.achievement.event;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InviteSentEvent {

    @NotNull
    private Long userId;

    @NotNull
    private Long receiverId;

    @NotNull
    private Long projectId;
}
