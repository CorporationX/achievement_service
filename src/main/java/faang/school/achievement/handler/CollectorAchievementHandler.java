package faang.school.achievement.handler;

import faang.school.achievement.messaging.events.GoalAttachedEvent;

public class CollectorAchievementHandler implements EventHandler<GoalAttachedEvent> {
    private static final String ACHIEVEMENT_NAME = "COLLECTOR";

    @Override
    public void handle(GoalAttachedEvent event) {

    }
}
