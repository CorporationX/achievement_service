package faang.school.achievement.handler.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.event.PostCreateEventDto;
import faang.school.achievement.mapper.AchievementDtoMapper;
import faang.school.achievement.mapper.AchievementProgressDtoMapper;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class WriterAchievementHandler extends PostCreateEventHandler {
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementDtoMapper achievementDtoMapper;
    private final AchievementProgressDtoMapper achievementProgressDtoMapper;
    private List<AchievementDto> achievements = new ArrayList<>();
    private List<AchievementProgressDto> achievementProgresses = new ArrayList<>();

    @Override
    public void handle(PostCreateEventDto event) {
        log.info ("UserId: {}.", event.getUserId());
        // achievementRepository.findAll().forEach(acheivement -> {
        //     achievements.add(achievementDtoMapper.toDto(acheivement));
        // });
        // achievements.stream().forEach(achievement -> {
        //     log.info("Achievement: {}", achievement);
        // });

        achievementProgressRepository.findByUserId(event.getUserId()).forEach(achievementProgress -> {
            achievementProgresses.add(achievementProgressDtoMapper.toDto(achievementProgress));
        });
        achievementProgresses.forEach(achievementProgress -> {
            log.info("AchievementProgress: {}", achievementProgress);
        });

        // userAchievementRepository.findByUserId(event.getUserId()).forEach(userAchievement -> {
        //     log.info("UserAchievement: {}", userAchievement);
        // });
    }
}
