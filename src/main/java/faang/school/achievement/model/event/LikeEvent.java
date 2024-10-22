package faang.school.achievement.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikeEvent(
        long postId,
        long likeAuthorId,
        long postAuthorId,
        LocalDateTime likedTime
) implements AuthorSearcher {

    @Override
    public long getAuthorForAchievements() {
        return likeAuthorId;
    }

}
