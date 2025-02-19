package faang.school.achievement.controller;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementCache achievementCache;

    @GetMapping("/{title}")
    public String get(@PathVariable String title) {
        Achievement achievement = achievementCache.get(title);
        return achievement.getTitle();
    }
}
