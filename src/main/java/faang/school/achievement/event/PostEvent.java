package faang.school.achievement.event;

import lombok.Builder;

@Builder
public record PostEvent(long authorId, long postId) implements AuthorSearches {

    @Override
    public long getAuthorForAchievements() {
        return authorId;
    }
}
