package faang.school.achievement.dto.achievement;

public record AchievementProgressReadDto(
        long id,
        String title,
        long currentPoints
) {
}
