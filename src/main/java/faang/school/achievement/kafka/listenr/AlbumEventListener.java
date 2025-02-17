package faang.school.achievement.kafka.listenr;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.mapper.album.AchievementMapper;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumEventListener {

    private final AchievementService achievementService;
    private final AchievementMapper achievementMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.album.topic}", groupId = "achievement-group")
    public void listen(String input) {

    }
}
