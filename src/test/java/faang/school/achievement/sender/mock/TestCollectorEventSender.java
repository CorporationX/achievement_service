package faang.school.achievement.sender.mock;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.sender.AchievementEventSender;
import faang.school.achievement.sender.AchievementEventType;

public class TestCollectorEventSender implements AchievementEventSender {
    @Override
    public void send(AchievementEventDto achievementEvent) {
        achievementEvent.setDescription("TestCollectorEventSender");
    }

    @Override
    public String getTitle() {
        return AchievementEventType.COLLECTOR.getTitle();
    }
}
