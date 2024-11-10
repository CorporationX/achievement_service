package faang.school.achievement.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileEventDto(Long userId,
                              LocalDateTime timestamp) {
}