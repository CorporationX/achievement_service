package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEvent {
    private long commentAuthorId;
    private long postId;
    private long likeId;
}