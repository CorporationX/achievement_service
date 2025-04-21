package faang.school.achievement.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeEvent {
    private long commentId;
    private long authorId;
    private long postId;

}