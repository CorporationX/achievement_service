package faang.school.achievement.event.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CommentEvent {

    private Long postCreatorId;
    private Long commenterId;
}