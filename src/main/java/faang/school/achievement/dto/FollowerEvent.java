package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent {
    private Long followeeId;
    private Long followerId;
    private LocalDateTime followedAt;
}
