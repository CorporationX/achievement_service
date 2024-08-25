package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievement")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;



    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<UserAchievementDto> getUserAchievements() {
        return achievementService.getAchievementsByUserId();
    }

    @GetMapping("/{achievementId}")
    @ResponseStatus(HttpStatus.OK)
    public AchievementDto getAchievement(@PathVariable Long achievementId) {
        return achievementService.getAchievementById(achievementId);
    }

    @GetMapping("/user/not-attained")
    @ResponseStatus(HttpStatus.OK)
    public List<AchievementProgressDto> getUserNotAttainedAchievements() {
        return achievementService.getUserNotAttainedAchievements();
    }

    @GetMapping("/users/progress")
    public List<AchievementProgressDto> getAchievementProgressByUserId() {
        return achievementService.getAchievementProgressByUserId();
    }

    @GetMapping("/title")
    public AchievementDto getAchievementByTitle(@RequestParam String title) {
        return achievementService.getAchievementByTitle(title);
    }
}
