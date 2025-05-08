package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.interfaces.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/achievements")
    public ResponseEntity<List<AchievementDto>> getAchievements(@ModelAttribute AchievementFilterDto filterDto,
                                                                @RequestHeader("x-user-id") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.getFilteredAchievements(filterDto));
    }

    @GetMapping("/users/{userId}/achievements")
    public ResponseEntity<List<UserAchievementDto>> getUserAchievments(@PathVariable("userId") long id,
                                                                       @RequestHeader("x-user-id") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.getAchievementsByUserId(userId));
    }

    @GetMapping("/achievements/{achievementId}")
    public ResponseEntity<AchievementDto> getAchievement(@PathVariable("achievementId") long achievementId,
                                                         @RequestHeader("x-user-id") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.getAchievementById(achievementId));
    }

    @GetMapping("/users/{userId}/achievement-progress")
    public ResponseEntity<List<UserAchievementDto>> getUserAchievementProgress(@PathVariable("userId") long id,
                                                                               @RequestHeader("x-user-id") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.getAchievementsProgressByUserId(userId));
    }
}
