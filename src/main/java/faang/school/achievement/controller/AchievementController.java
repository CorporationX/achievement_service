package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/all")
    public List<AchievementDto> findAll() {
        return achievementService.findAll();
    }

    @PostMapping("/filter")
    public List<AchievementDto> filter(@RequestBody AchievementFilterDto filter) {
        return achievementService.findFilteredAchievements(filter);
    }

}
