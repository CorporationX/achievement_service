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

import java.util.Collections;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/achievement")
public class AchievementRequestController {

    private final AchievementRequestService achievementRequestService;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAllAchievements(@Valid AchievementFilterDto filter) {
        return toListResponse(achievementRequestService.getAllAchievements(filter));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievementDto>> getUserAchievements(@PathVariable @Positive Long userId) {
        return toListResponse(achievementRequestService.getUserAchievements(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(achievementRequestService.getAchievementById(id));
    }

    @GetMapping("/user/{userId}/unearned")
    public ResponseEntity<List<AchievementProgressDto>> getUserUnearnedAchievements(
            @PathVariable @Positive Long userId
    ) {
        return toListResponse(achievementRequestService.getUserUnearnedAchievements(userId));
    }

    private <T> ResponseEntity<List<T>> toListResponse(List<T> list) {
        if (list == null || list.isEmpty()) {
            return ResponseEntity.ok().body(Collections.emptyList());
        }
        return ResponseEntity.ok().body(list);
    }
}