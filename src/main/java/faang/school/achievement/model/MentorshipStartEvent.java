package faang.school.achievement.model;

public record MentorshipStartEvent(
        long mentorId,
        long menteeId
) {
}