package faang.school.achievement.controller;


import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.service.achievement_progress.AchievementProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements/progress")
@RequiredArgsConstructor
public class AchievementProgressController {

    private final AchievementProgressService achievementProgressService;

    @GetMapping("/{userId}")
    public List<AchievementProgressDto> getAchievementProgressesByUserId(@PathVariable long userId) {
        return achievementProgressService.getAchievementProgressesByUserId(userId);
    }
}
