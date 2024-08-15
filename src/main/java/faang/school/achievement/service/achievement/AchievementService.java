package faang.school.achievement.service.achievement;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.filter.AchievementFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class AchievementService {

    private final UserContext userContext;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;
    private final List<AchievementFilter> achievementFilters;

    public List<AchievementDto> getAchievementsByFilter(AchievementFilterDto filterDto) {
        Stream<Achievement> achievementStream = StreamSupport.stream(achievementRepository.findAll().spliterator(), false);
        achievementStream = achievementFilters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .reduce(
                        achievementStream,
                        (stream, filter) -> filter.apply(stream, filterDto),
                        (s1, s2) -> s1
                );
        return achievementStream.map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserAchievementDto> getUserAchievementsByUserId() {
        long userId = userContext.getUserId();
        return userAchievementRepository.findByUserId(userId).stream()
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public AchievementDto getAchievementById(long achievementId) {
        var achi = achievementRepository.findById(achievementId).orElseThrow(() -> new IllegalArgumentException("Achievement with id " + achievementId + " doesn't exist"));
        return achievementMapper.toDto(achi);
    }

    public List<AchievementProgressDto> getInProgressUserAchievementsByUserId() {
        long userId = userContext.getUserId();
        return achievementProgressRepository.findByUserId(userId).stream()
                .filter(achi -> achi.getCurrentPoints() >= achi.getAchievement().getPoints())
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }
}
