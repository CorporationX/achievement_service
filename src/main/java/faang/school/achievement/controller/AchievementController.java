package faang.school.achievement.controller;

import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
}
