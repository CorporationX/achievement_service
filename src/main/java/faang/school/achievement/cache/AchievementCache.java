package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {
    private Map<String, Achievement> achievements;

    private final AchievementRepository achievementRepository;

    @PostConstruct
    private void init() {
        achievements = new HashMap<>();
        achievements = achievementRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Achievement::getTitle,
                        achievement -> achievement
                ));
        log.info("Кэш Достижений успешно инициализирован");

    }

    public Achievement get(String achievementTitle) {
        Achievement achievement = achievements.get(achievementTitle);
        if (achievement == null) {
            achievement = achievementRepository.findByTitleIgnoreCase(achievementTitle)
                    .orElseThrow(() -> new EntityNotFoundException("Достижения с названием " + achievementTitle
                            + " не существует"));
        }
        return achievement;
    }
}