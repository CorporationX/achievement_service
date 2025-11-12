package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.service.AchievementServiceImpl;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
@Validated
public class AchievementController {
    private final AchievementServiceImpl achievementServiceImpl;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAchievements(
            @RequestParam(required = false) @Size(max = 128) String title,
            @RequestParam(required = false) @Size(max = 1024) String description,
            @RequestParam(required = false) @Size(max = 50) String rarity
    ) {
        return ResponseEntity.ok(achievementServiceImpl.getFilteredAchievements(title, description, rarity));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementDto>> getUserAchievements(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return ResponseEntity.ok(achievementServiceImpl.getUserAchievements(userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(
            @PathVariable @NotNull @Positive Long id
    ) {
        return ResponseEntity.ok(achievementServiceImpl.getAchievementById(id));
    }

    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<List<AchievementProgressDto>> getUnearnedAchievementsWithProgress(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return ResponseEntity.ok(achievementServiceImpl.getUnearnedAchievementsWithProgress(userId));
    }
}
