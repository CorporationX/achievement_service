package faang.school.achievement.sender;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AchievementEventType {
    WRITER("WRITER"),
    COLLECTOR("COLLECTOR");

    private final String title;
}
