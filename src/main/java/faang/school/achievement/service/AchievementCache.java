package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AchievementCache {
    private final Map<String, Achievement> achievementCash = new HashMap<>();
    private final AchievementRepository repository;

    @PostConstruct
    private void init() {
        repository.findAll().forEach(achievement -> {
            achievementCash.put(achievement.getTitle(), achievement);
        });
    }

    private Achievement get(String title) {
        return achievementCash.get(title);
    }

}

