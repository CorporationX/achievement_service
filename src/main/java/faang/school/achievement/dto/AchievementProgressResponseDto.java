package faang.school.achievement.dto;

public record AchievementProgressResponseDto(
        AchievementResponseDto achievement,
        long currentPoints
) {

}
