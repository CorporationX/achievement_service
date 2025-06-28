package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record ProgressCreationResultDto(long userId, long achievementId, boolean created) {
}