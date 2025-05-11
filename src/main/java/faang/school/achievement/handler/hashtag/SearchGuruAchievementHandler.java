package faang.school.achievement.handler.hashtag;

import faang.school.achievement.dto.HashtagRequestEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchGuruAchievementHandler implements EventHandler<HashtagRequestEvent> {

    private static final String TITLE_ACHIEVEMENT = "SEARCH GURU";

    private final AbstractEventHandler eventHandler;

    @Async("hashtagHandleAsync")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(HashtagRequestEvent event) {
        eventHandler.processAchievement(event, TITLE_ACHIEVEMENT, event.userId());
    }
}
