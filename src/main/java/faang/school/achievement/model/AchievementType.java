package faang.school.achievement.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchievementType {

    SKILL_KEEPER("SKILL KEEPER");

    private final String title;
}
