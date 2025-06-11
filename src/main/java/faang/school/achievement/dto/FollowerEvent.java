package faang.school.achievement.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FollowerEvent {

    private Long followerId;
    private Long followeeId;
    private LocalDateTime timestamp;
}

