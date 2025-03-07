package faang.school.achievement.event;

import lombok.Data;

@Data
public class InviteSentEvent {
    private Long userId;
    private Long receiverId;
    private Long projectId;
}
