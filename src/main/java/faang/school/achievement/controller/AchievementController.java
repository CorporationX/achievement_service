package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
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

    private final AchievementServiceImpl achievementServiceImpl;

    @GetMapping
    public List<AchievementDto> getAchievements() {
        return achievementServiceImpl.getAllAchievements();
    }

    @GetMapping("/user/{id}")
    public List<UserAchievementDto> getAchievementsByUserId(@PathVariable Long id) {
        return achievementServiceImpl.getAllUserAchievements(id);
    }

    @GetMapping("/{id}")
    public AchievementDto getAchievementById(@PathVariable Long id) {
        return achievementServiceImpl.getAchievement(id);
    }

    @GetMapping("/user/progress/{id}")
    public List<AchievementProgressDto> GETUserAchievementProgress(@PathVariable Long id) {
        return achievementServiceImpl.getUserAchievementProgress(id);
    }
}
