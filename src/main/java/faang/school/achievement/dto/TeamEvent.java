package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record TeamEvent(
        Long creatorId,
        Long projectId,
        Long teamId
) {
}
