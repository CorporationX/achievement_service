package faang.school.achievement.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SessionFactory sessionFactory;

    @PostConstruct
    public void initCache() {
        List<Achievement> allAchievements = getAchievements();
        objectMapper.registerModule(new JavaTimeModule());

        allAchievements.forEach(achievement -> {
            try {
                String achievementJson = objectMapper.writeValueAsString(achievement);
                redisTemplate.opsForValue().set(achievement.getTitle(), achievementJson);
            } catch (Exception e) {
                log.error("Ошибка преобразования объекта в JSON", e);
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    @Transactional
    public List<Achievement> getAchievements() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Achievement> achievements = session.createQuery("from Achievement", Achievement.class).getResultList();

        for (Achievement achievement : achievements) {
            Hibernate.initialize(achievement.getUserAchievements());
            Hibernate.initialize(achievement.getProgresses());
            Hibernate.initialize(achievement.getRarity());
        }

        session.getTransaction().commit();
        session.close();

        return achievements;
    }

    @Transactional
    public Achievement getAchievementByTitle(String title) {
        Object achievement = redisTemplate.opsForValue().get(title);
        if (achievement == null) {
            achievement = achievementRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException(
                    String.format("Достижение с названием %s не найдено", title)));
        }

        return objectMapper.convertValue(achievement, Achievement.class);
    }
}
