package faang.school.achievement.sender;

import faang.school.achievement.dto.event.AchievementEventDto;

public interface AchievementEventSender {

    void send(AchievementEventDto achievementEvent);
    String getTitle();
}
