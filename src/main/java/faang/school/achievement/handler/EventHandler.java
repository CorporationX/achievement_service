package faang.school.achievement.handler;

import faang.school.achievement.dto.event.CommentEvent;

public interface EventHandler {
    void handle(CommentEvent event);
}
