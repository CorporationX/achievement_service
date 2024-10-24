package faang.school.achievement.dto.achievement.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.achievement.dto.achievement.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentEventDto extends AbstractEvent {

    private Long postId;
    private Long commentId;
    private String commentText;

    @Override
    @JsonProperty("commentAuthorId")
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
