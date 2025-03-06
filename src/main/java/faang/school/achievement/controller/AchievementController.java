package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService service;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAllAchievements(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String rarity) {
        List<AchievementDto> achievements = service.getAllAchievements(name, description, rarity);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievementDto>> getUserAchievements(@PathVariable Long userId) {
        List<UserAchievementDto> achievements = service.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(@PathVariable Long id) {
        AchievementDto achievement = service.getAchievementById(id);
        return ResponseEntity.ok(achievement);
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<AchievementProgressDto>> getUserPendingAchievements(@PathVariable Long userId) {
        List<AchievementProgressDto> pendingAchievements = service.getUserPendingAchievements(userId);
        return ResponseEntity.ok(pendingAchievements);
    }
}