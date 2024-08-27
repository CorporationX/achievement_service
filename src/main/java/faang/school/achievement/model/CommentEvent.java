package faang.school.achievement.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEvent {
    @NotNull
    private long authorId;
    @NotNull
    private long postId;
    @NotNull
    private long commentId;
    @NotNull
    private String comment;
}
