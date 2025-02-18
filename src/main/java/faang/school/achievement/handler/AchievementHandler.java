package faang.school.achievement.handler;

import faang.school.achievement.dto.album.AlbumCreatedEvent;

public interface AchievementHandler {

    void applyAchievement(AlbumCreatedEvent event);

}
