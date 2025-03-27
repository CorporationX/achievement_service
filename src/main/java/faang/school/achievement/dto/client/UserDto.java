package faang.school.achievement.dto.client;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String email,
        boolean active
) {
}