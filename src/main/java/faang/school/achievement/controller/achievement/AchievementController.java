package faang.school.achievement.controller.achievement;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/achievements/")
public class AchievementController {

    private final AchievementService achievementService;
    private final UserContext userContext;

    @GetMapping("{achievementId}")
    public AchievementProgressDto getAchievementStats(@PathVariable long achievementId) {
        long userId = userContext.getUserId();
        return achievementService.getAchievementProgress(userId, achievementId);
    }
}
