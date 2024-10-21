package faang.school.achievement.model.event;

import lombok.Builder;

@Builder
public record ManagerAchievementEvent(
        long authorId,
        long teamId,
        long projectId
) implements AuthorSearcher {
    @Override
    public long getAuthorForAchievements() {
        return authorId;
    }
}
