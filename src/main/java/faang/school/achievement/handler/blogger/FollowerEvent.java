package faang.school.achievement.handler.blogger;

import lombok.Data;

@Data
public class FollowerEvent {
    private Long followerId;
    private Long followeeId;
}
