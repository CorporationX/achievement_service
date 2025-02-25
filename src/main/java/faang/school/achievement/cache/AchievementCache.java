package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> achievements;

    public Achievement get(String title) {
        return achievements.get(title);
    }

    @PostConstruct
    @Transactional
    public void init() {
        achievements = achievementRepository.findAllWithLazyCollections()
                .stream()
                .collect(Collectors.toMap(Achievement::getTitle, Function.identity()));
    }
}
