package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/achievement")
public class AchievementRequestController {

    private final AchievementRequestService achievementRequestService;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAllAchievements(@Valid AchievementFilterDto filter) {
        List<AchievementDto> achievements = achievementRequestService.getAllAchievements(filter);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievementDto>> getUserAchievements(@PathVariable @Positive Long userId) {
        List<UserAchievementDto> achievements = achievementRequestService.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(achievementRequestService.getAchievementById(id));
    }

    @GetMapping("/user/{userId}/unearned")
    public ResponseEntity<List<AchievementProgressDto>> getUserUnearnedAchievements(
            @PathVariable @Positive Long userId
    ) {
        List<AchievementProgressDto> achievements = achievementRequestService.getUserUnearnedAchievements(userId);
        return ResponseEntity.ok(achievements);
    }
}