package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/achievements")
@RestController
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/{title}")
    public AchievementDto getByTitle(@PathVariable String title) {
        return achievementService.getByTitle(title);
    }
}
