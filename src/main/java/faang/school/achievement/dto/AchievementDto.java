package faang.school.achievement.dto;

public record AchievementDto(
        Long id,
        String title,
        String description,
        String rarity,
        Long points
) {
}
