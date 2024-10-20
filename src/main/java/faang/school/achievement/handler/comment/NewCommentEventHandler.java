package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.comment.NewCommentEventDto;
import faang.school.achievement.handler.NewEventHandler;
import org.springframework.stereotype.Component;

@Component
public abstract class NewCommentEventHandler implements NewEventHandler {
    public abstract void verifyAchievement(NewCommentEventDto newCommentEventDto);
}
