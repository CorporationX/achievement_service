package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/achievement")
@RequiredArgsConstructor
public class AchievementController {

    private final UserContext userContext;
    private final AchievementService achievementService;

    @PostMapping
    public List<AchievementDto> getAchievements(@RequestBody AchievementFilterDto filter) {
        log.info("Get achievements by filter: {}", filter);
        return achievementService.getAchievements(filter);
    }

    @GetMapping
    public List<AchievementDto> getAchievements() {
        long userId = userContext.getUserId();
        log.info("Get achievements by user: {}", userId);
        return achievementService.getAchievementsBy(userId);
    }

    @GetMapping("/{achievementId}")
    public AchievementDto getAchievement(@PathVariable @Positive Long achievementId) {
        log.info("Get achievement: {}", achievementId);
        return achievementService.getAchievement(achievementId);
    }

    @GetMapping("/not-received")
    public List<AchievementDto> getNotReceivedAchievements() {
        long userId = userContext.getUserId();
        log.info("Get not-received achievements by user: {}", userId);
        return achievementService.getNotReceivedAchievements(userId);
    }
}
