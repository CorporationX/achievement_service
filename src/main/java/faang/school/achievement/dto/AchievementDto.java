package faang.school.achievement.dto;

public record AchievementDto(
        long id,
        String title,
        int requiredPoints
) {}