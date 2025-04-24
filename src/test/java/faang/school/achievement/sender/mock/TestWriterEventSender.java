package faang.school.achievement.sender.mock;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.sender.AchievementEventSender;

public class TestWriterEventSender implements AchievementEventSender {
    @Override
    public void send(AchievementEventDto achievementEvent) {
        achievementEvent.setDescription("TestWriterEventSender");
    }

    @Override
    public String getTitle() {
        return "WRITER";
    }
}
