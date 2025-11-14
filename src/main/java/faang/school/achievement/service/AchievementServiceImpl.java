package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.filters.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static faang.school.achievement.utils.Utils.stringFormatting;

@Component
@RequiredArgsConstructor
@Validated
public class AchievementServiceImpl implements AchievementService {
    private static final long ZERO_VALUE = 0L;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final List<AchievementFilter> achievementFilters;

    /**
     * Получение всех достижений пользователя с данным id.
     */
    @Override
    public List<AchievementResponseDto> getAllAchievementsUser(@Positive long userId) {
        return userAchievementRepository
                .findByUserId(userId)
                .stream()
                .map(UserAchievement::getAchievement)
                .map(achievementMapper::toAchievementResponseDto)
                .toList();
    }

    /**
     * Получение всех возможных достижений в системе. При получении достижения,
     * их можно фильтровать по названию, описанию и редкости
     */

    @Override
    public List<AchievementResponseDto> getAllAchievements(AchievementFilterDto filterDto) {
        Stream<Achievement> achievements = StreamSupport.stream(achievementRepository.findAll().spliterator(), false);

        for (AchievementFilter achievementFilter : achievementFilters) {
            if (achievementFilter.isApplicable(filterDto)) {
                achievements = achievementFilter.apply(achievements, filterDto);
            }
        }
        return achievements
                .map(achievementMapper::toAchievementResponseDto)
                .toList();
    }

    /**
     * Получение достижения по его id.
     */
    @Override
    public AchievementResponseDto getAchievementsUserById(@Positive long achievementsId) {
        Optional<Achievement> achievementFound = achievementRepository.findById(achievementsId);
        if (achievementFound.isEmpty()) {
            throw new EntityNotFoundException(stringFormatting("Achievement Id {} not found", achievementsId));
        }
        Achievement userAchievement = achievementFound.get();
        return achievementMapper.toAchievementResponseDto(userAchievement);
    }

    /**
     * Получение всех неполученных достижений пользователя с их прогрессом. Получение происходит по id пользователя
     */
    @Override
    public List<AchievementProgressResponseDto> getUserPendingAchievementsWithProgress(@Positive long userId) {
        List<AchievementResponseDto> allAchievements = getAllAchievements(new AchievementFilterDto(
                null, null, null));

        List<AchievementResponseDto> allAchievementsUser = getAllAchievementsUser(userId);

        List<AchievementProgress> userAchievementProgress = achievementProgressRepository.findByUserId(userId);


        Set<Long> allAchievementsUserId = allAchievementsUser.stream()
                .map(AchievementResponseDto::id)
                .collect(Collectors.toSet());

        List<AchievementProgressResponseDto> userAchievementProgressCurrent = userAchievementProgress.stream()
                .filter(item -> !allAchievementsUserId.contains(item.getAchievement().getId()))
                .map(achievementProgressMapper::toAchievementProgressResponseDto)
                .toList();

        List<Long> achievementProgressCurrentId = userAchievementProgressCurrent.stream()
                .map(itemDto -> itemDto.achievement().id())
                .toList();

        List<AchievementProgressResponseDto> zeroAchievementProgress = allAchievements.stream()
                .filter(item -> !allAchievementsUserId.contains(item.id()))
                .filter(item -> !achievementProgressCurrentId.contains(item.id()))
                .map(item -> new AchievementProgressResponseDto(item, ZERO_VALUE))
                .toList();

        return Stream.of(zeroAchievementProgress, userAchievementProgressCurrent)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(AchievementProgressResponseDto::currentPoints).reversed())
                .toList();
    }
}