package faang.school.achievement.model.event;

import java.time.LocalDateTime;

public record MentorshipStartEvent (
        long mentorId,
        long menteeId,
        LocalDateTime timestamp
) {}