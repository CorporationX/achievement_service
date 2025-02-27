package faang.school.achievement.cashe;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final Map<String, Achievement> achievements = new ConcurrentHashMap<>();

    public Achievement get(String name) {
        return Optional.ofNullable(achievements.get(name))
                .orElseThrow(() -> new EntityNotFoundException("Достижение с именем '" + name + "' не найдено"));
    }

    @PostConstruct
    public void init() {
        achievementRepository.findAll()
                .forEach(achievement ->
                        achievements.put(achievement.getTitle(), achievement));
        log.info("Кэш достижений инициализирован");
    }
}
