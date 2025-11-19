package faang.school.achievement.service;


import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;

import java.util.List;


public abstract class DataForTests {

    protected static final long USER_ID_2 = 2L;
    protected static final long UNKNOWN_ID = Long.MAX_VALUE;
    protected static final long ACHIEVEMENT_ID_1 = 1L;
    protected static final long ACHIEVEMENT_ID_2 = 2L;
    protected static final long ACHIEVEMENT_ID_3 = 3L;

    protected static final Long USER_ID_1 = 1L;
    protected static final String DESCRIPTION = "100";
    protected static final String ACHIEVEMENT_TITLE_COLLECTOR = "COLLECTOR";
    protected static final String ACHIEVEMENT_TITLE_EXPERT = "EXPERT";
    protected static final String UNKNOWN_ACHIEVEMENT_TITLE = "ONLY ACHIEVEMENT";

    protected Achievement achievementCollector = Achievement.builder()
            .id(ACHIEVEMENT_ID_1)
            .title("COLLECTOR")
            .description("For 100 goals")
            .rarity(Rarity.EPIC)
            .points(15L)
            .build();
    protected Achievement achievementMrProductivity = Achievement.builder()
            .id(ACHIEVEMENT_ID_2)
            .title("MR PRODUCTIVITY")
            .description("For 1000 finished tasks")
            .rarity(Rarity.LEGENDARY)
            .points(20L)
            .build();
    protected Achievement achievementExpert = Achievement.builder()
            .id(3L)
            .title("EXPERT")
            .description("For 1000 comments")
            .rarity(Rarity.UNCOMMON)
            .points(5L)
            .build();
    protected Achievement achievementSensei = Achievement.builder()
            .id(4L)
            .title("SENSEI")
            .description("For 30 mentees")
            .rarity(Rarity.LEGENDARY)
            .points(20L)
            .build();
    protected Achievement achievementManager = Achievement.builder()
            .id(5L)
            .title("MANAGER")
            .description("For 10 teams")
            .rarity(Rarity.RARE)
            .points(10L)
            .build();
    protected Achievement achievementCelebrity = Achievement.builder()
            .id(6L)
            .title("CELEBRITY")
            .description("For 1 000 000 subscribers")
            .rarity(Rarity.LEGENDARY)
            .points(20L)
            .build();
    protected Achievement achievementWriter = Achievement.builder()
            .id(7L)
            .title("WRITER")
            .description("For 100 posts published")
            .rarity(Rarity.RARE)
            .points(10L)
            .build();
    protected Achievement achievementHandsome = Achievement.builder()
            .id(8L)
            .title("HANDSOME")
            .description("For uploaded profile photo")
            .rarity(Rarity.COMMON)
            .points(2L)
            .build();
    protected List<Achievement> allAchievements = List.of(
            achievementCollector,
            achievementMrProductivity,
            achievementExpert,
            achievementSensei,
            achievementManager,
            achievementCelebrity,
            achievementWriter,
            achievementHandsome
    );

    protected UserAchievement userAchievementId2 = UserAchievement.builder()
            .id(USER_ID_2)
            .userId(USER_ID_2)
            .achievement(achievementMrProductivity)
            .build();

    protected UserAchievement userAchievementId3 = UserAchievement.builder()
            .userId(USER_ID_2)
            .achievement(achievementHandsome)
            .build();

    protected AchievementProgress achievementProgressId3CurrentPoints4 = AchievementProgress
            .builder()
            .id(3L)
            .userId(USER_ID_1)
            .achievement(achievementExpert)
            .currentPoints(4L)
            .build();

    protected AchievementProgress achievementProgressId3CurrentPoints0 = AchievementProgress
            .builder()
            .id(3L)
            .userId(USER_ID_1)
            .achievement(achievementExpert)
            .currentPoints(0L)
            .build();

    protected AchievementProgress achievementProgressId7CurrentPoints8 = AchievementProgress
            .builder()
            .id(7L)
            .userId(USER_ID_1)
            .achievement(achievementWriter)
            .currentPoints(8L)
            .build();

    protected AchievementProgress achievementProgressId7CurrentPoints0 = AchievementProgress
            .builder()
            .id(7L)
            .userId(USER_ID_1)
            .achievement(achievementWriter)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId2CurrentPoints15 = AchievementProgress
            .builder()
            .id(2L)
            .userId(USER_ID_1)
            .achievement(achievementMrProductivity)
            .currentPoints(15L)
            .build();

    protected AchievementProgress achievementProgressId2CurrentPoints0 = AchievementProgress
            .builder()
            .id(2L)
            .userId(USER_ID_1)
            .achievement(achievementMrProductivity)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId1CurrentPoints15 = AchievementProgress
            .builder()
            .id(1L)
            .userId(USER_ID_1)
            .achievement(achievementCollector)
            .currentPoints(15L)
            .build();

    protected AchievementProgress achievementProgressId1CurrentPoints0 = AchievementProgress
            .builder()
            .id(1L)
            .userId(USER_ID_1)
            .achievement(achievementCollector)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId4CurrentPoints0 = AchievementProgress
            .builder()
            .id(4L)
            .userId(USER_ID_1)
            .achievement(achievementSensei)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId5CurrentPoints0 = AchievementProgress
            .builder()
            .id(5L)
            .userId(USER_ID_1)
            .achievement(achievementManager)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId6CurrentPoints0 = AchievementProgress
            .builder()
            .id(6L)
            .userId(USER_ID_1)
            .achievement(achievementCelebrity)
            .currentPoints(0)
            .build();

    protected AchievementProgress achievementProgressId8CurrentPoints0 = AchievementProgress
            .builder()
            .id(8L)
            .userId(USER_ID_1)
            .achievement(achievementHandsome)
            .currentPoints(0)
            .build();

    protected List<AchievementProgress> allAchievementProgressCurrentPoints0 = List.of(
            achievementProgressId3CurrentPoints0,
            achievementProgressId7CurrentPoints0,
            achievementProgressId2CurrentPoints0,
            achievementProgressId1CurrentPoints0,
            achievementProgressId4CurrentPoints0,
            achievementProgressId5CurrentPoints0,
            achievementProgressId6CurrentPoints0,
            achievementProgressId8CurrentPoints0
    );
}