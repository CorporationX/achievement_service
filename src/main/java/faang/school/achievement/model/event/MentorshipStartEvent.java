package faang.school.achievement.model.event;

import lombok.Builder;

@Builder
public record MentorshipStartEvent(long mentorId, long menteeId) implements AuthorSearcher {

    @Override
    public long getAuthorForAchievements() {
        return mentorId;
    }
}
