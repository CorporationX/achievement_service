package faang.school.achievement.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HashtagRequestEvent(
        Long userId,
        Long hashtagId,
        LocalDateTime receivedAt
) {
}
