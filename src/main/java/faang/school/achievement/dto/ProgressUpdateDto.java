package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record ProgressUpdateDto(long progressId, long currentPoints) {
}