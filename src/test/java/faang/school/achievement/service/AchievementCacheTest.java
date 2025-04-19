package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AchievementCacheTest {

    private AchievementRepository achievementRepository;
    private AchievementCache achievementCache;

    @BeforeEach
    void setUp() {
        achievementRepository = mock(AchievementRepository.class);
        Achievement achievement = faang.school.achievement.model.Achievement.builder()
                .id(1L)
                .title("COLLECTOR")
                .description("For 100 goals")
                .rarity(Rarity.RARE)
                .points(15)
                .build();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        achievementCache.init();
    }

    @Test
    public void returnAchievmentFromCashByTitle() {
        Achievement result = achievementCache.get("COLLECTOR");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("COLLECTOR");
        assertThat(result.getRarity()).isEqualTo(Rarity.RARE);

    }

}
