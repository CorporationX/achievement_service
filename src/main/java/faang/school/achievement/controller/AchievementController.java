package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.AchievementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementDto> getAchievements() {
        return achievementService.getAllAchievements();
    }

    @GetMapping("/user/{id}")
    public List<UserAchievementDto> getAchievementsByUserId(@PathVariable Long id) {
        return achievementService.getAllUserAchievements(id);
    }

    @GetMapping("/{id}")
    public AchievementDto getAchievementById(@PathVariable Long id) {
        return achievementService.getAchievement(id);
    }

    @GetMapping("/user/{id}/progress")
    public List<AchievementProgressDto> GetUserAchievementProgress(@PathVariable Long id) {
        return achievementService.getUserAchievementProgress(id);
    }
}
