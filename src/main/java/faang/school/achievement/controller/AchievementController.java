package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.service.AchievementService;
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
@RequestMapping("/achievement")
@RequiredArgsConstructor
@Validated
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAchievements(
            @Valid AchievementFilterDto filterDto) {
        return ResponseEntity.ok(achievementService.getFilteredAchievements(filterDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementDto>> getUserAchievements(
            @PathVariable @Positive Long userId
    ) {
        return ResponseEntity.ok(achievementService.getUserAchievements(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<List<AchievementProgressDto>> getUnearnedAchievementsWithProgress(
            @PathVariable @Positive Long userId
    ) {
        return ResponseEntity.ok(achievementService.getUnearnedAchievementsWithProgress(userId));
    }
}
