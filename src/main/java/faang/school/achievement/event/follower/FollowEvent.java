package faang.school.achievement.event.follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowEvent {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime createdAt;
}
