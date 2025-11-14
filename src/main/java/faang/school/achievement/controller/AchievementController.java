package faang.school.achievement.controller;


import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.model.UserAchievementStatus;
import faang.school.achievement.service.AchievementServiceImpl;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AchievementController {
    private final AchievementServiceImpl achievementService;

    @PostMapping("/achievements")
    public List<AchievementResponseDto> getAllAchievements(@RequestBody AchievementFilterDto filterDto) {
        return achievementService.getAllAchievements(filterDto);
    }

    @GetMapping("/users/{userId}/achievements")
    public List<AchievementResponseDto> getAllAchievementsUser(@PathVariable @Positive long userId) {
        return achievementService.getAllAchievementsUser(userId);
    }

    @GetMapping("/achievements/{achievementId}")
    public AchievementResponseDto getAchievementsUserById(@PathVariable @Positive long achievementId) {
        return achievementService.getAchievementsById(achievementId);
    }

    @GetMapping("/users/{userId}/achievements/progress")
    public List<AchievementProgressResponseDto> getUserPendingAchievementsWithProgress(
            @PathVariable @Positive long userId,
            UserAchievementStatus status) {
        return achievementService.getUserPendingAchievementsWithProgress(userId, status);
    }
}