package faang.school.achievement.event;

public record ProfilePicEvent(
        long userId,
        String profilePicKey
) {
}
