package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final UserContext userContext;

    @GetMapping("/completed")
    public List<UserAchievementDto> findAchievementsById() {
        return achievementService.findAchievementsByUserId(userContext.getUserId());
    }

    @GetMapping("/process")
    public List<AchievementProgressDto> findProcessingAchievementsByUserId() {
        return achievementService.findProcessingAchievementsByUserId(userContext.getUserId());
    }

    @GetMapping("/all")
    public List<AchievementDto> findAll() {
        return achievementService.findAll();
    }

    @PostMapping("/filter")
    public List<AchievementDto> filter(@RequestBody AchievementFilterDto filter) {
        return achievementService.findFilteredAchievements(filter);
    }

    @GetMapping("/{id}")
    public AchievementDto findById(@PathVariable long id) {
        return achievementService.findById(id);
    }

}
