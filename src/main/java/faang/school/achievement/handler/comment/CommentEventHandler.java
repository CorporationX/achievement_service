package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.CommentEvent;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class CommentEventHandler {
    private final AchievementService achievementService;

    protected void processAchievement(CommentEvent event, String title){

    }

}
