package faang.school.achievement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeEvent {
    private long authorId;
    private long postId;
    private long likeId;
}
