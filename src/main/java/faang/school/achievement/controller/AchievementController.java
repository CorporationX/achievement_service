package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

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
    public List<AchievementDto> getAllAchievements(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Rarity rarity) {
        return service.getAllAchievements(name, description, rarity);
    }

    @GetMapping("/user/{userId}")
    public List<UserAchievementDto> getUserAchievements(@PathVariable Long userId) {
        return service.getUserAchievements(userId);
    }

    @GetMapping("/{id}")
    public AchievementDto getAchievementById(@PathVariable Long id) {
        return service.getAchievementById(id);
    }

    @GetMapping("/user/{userId}/pending")
    public List<AchievementProgressDto> getUserPendingAchievements(@PathVariable Long userId) {
        return service.getUserPendingAchievements(userId);
    }
}