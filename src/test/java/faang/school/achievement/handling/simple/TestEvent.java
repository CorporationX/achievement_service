package faang.school.achievement.handling.simple;

public record TestEvent(
        long targetUserId,
        long performingUserId
) {}
