package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.event.CommentEventDto;
import faang.school.achievement.handler.EventHandler;

public abstract class CommentEventHandler implements EventHandler<CommentEventDto> {

    @Override
    public void handle(CommentEventDto event) {
        if (event.getAuthorId() == null || event.getCommentId() == null) {
            return;
        }
        process(event);
    }

    protected abstract void process(CommentEventDto event);
}
