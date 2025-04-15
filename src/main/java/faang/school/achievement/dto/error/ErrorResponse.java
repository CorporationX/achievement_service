package faang.school.achievement.dto.error;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message
) {
}
