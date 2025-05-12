package faang.school.achievement.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchievementType {

    COLLECTOR("COLLECTOR"),
    MR_PRODUCTIVITY("MR PRODUCTIVITY"),
    EXPERT("EXPERT"),
    SENSEI("SENSEI"),
    SKILL_KEEPER("SKILL KEEPER");

    private final String title;
}
