package faang.school.achievement.controller;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressReadDto;
import faang.school.achievement.dto.achievement.AchievementReadDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementReadDto> getAllAchievements(@RequestBody(required = false) AchievementFilterDto filterDto) {
        return achievementService.getAllAchievements(filterDto);
    }

    @GetMapping("/users/{userId}")
    public List<AchievementReadDto> getAchievementsByUserId(@PathVariable long userId) {
        return achievementService.getAchievementsByUserId(userId);
    }

    @GetMapping("/{achievementId}")
    public AchievementReadDto getAchievementById(@PathVariable long achievementId) {
        return achievementService.getAchievementById(achievementId);
    }

    @GetMapping("/users/{userId}/progress")
    public List<AchievementProgressReadDto> getAchievementProgressByUserId(long userId) {
        return achievementService.getAchievementProgressByUserId(userId);
    }
}
