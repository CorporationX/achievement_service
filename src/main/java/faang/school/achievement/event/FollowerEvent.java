package faang.school.achievement.event;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record FollowerEvent(
        @NotBlank Long followerId,
        @NotBlank Long followeeId,
        @NotBlank LocalDateTime timestamp) {
}
