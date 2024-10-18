package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.comment.CommentEventDto;
import org.springframework.stereotype.Component;

@Component
public abstract class CommentEventHandler {
    public abstract void verifyAchievement(CommentEventDto commentEventDto);
}
