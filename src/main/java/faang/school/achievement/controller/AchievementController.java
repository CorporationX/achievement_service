package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementDto> getAllAchievement(@RequestBody AchievementFilterDto filterDto) {
        return achievementService.getAllAchievement(filterDto);
    }

    @GetMapping("/user_achievements/{userId}")
    public List<UserAchievementDto> getAchievementsByUserId(@Positive @PathVariable Long userId) {
        return achievementService.getAchievementsByUserId(userId);
    }

    @GetMapping("/{id}")
    public AchievementDto getAchievement(@Positive @PathVariable Long id) {
        return achievementService.getAchievement(id);
    }

    @GetMapping("/progress/{userId}")
    public List <AchievementProgressDto> getAllAchievementsProgressForUser(@Positive @PathVariable Long userId) {
        return achievementService.getAllAchievementsProgressForUser(userId);
    }
}
