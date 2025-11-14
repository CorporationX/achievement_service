package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.source.ReloadableAchievementSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component("AchievementCache")
public class AchievementCache implements ReloadableAchievementSource {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> achievementCacheByTitle;
    private Map<Long, Achievement> achievementCacheById;

    @PostConstruct
    private void initCache() {
        reloadCache();
    }

    @Override
    public Optional<Achievement> getByTitle(String title) {
        return Optional.ofNullable(achievementCacheByTitle.get(title));
    }

    @Override
    public List<Achievement> getAll() {
        return achievementCacheByTitle
                .values()
                .stream()
                .toList();
    }

    @Override
    public void reloadCache() {
        Stream<Achievement> achievements = StreamSupport.stream(achievementRepository.findAll().spliterator(), false);

        achievementCacheByTitle = achievements
                .collect(Collectors.toMap(
                        Achievement::getTitle,
                        achievement -> achievement
                ));

        achievementCacheById = achievementCacheByTitle.values().stream()
                .collect(Collectors.toMap(
                        Achievement::getId,
                        achievement -> achievement
                ));
    }

    @Override
    public Optional<Achievement> getById(long id) {
        System.out.print("Взят из кеша");
        return Optional.ofNullable(achievementCacheById.get(id));
    }
}