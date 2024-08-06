package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.publisher.AchievementPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementPublisher publisher;

    @PutMapping("/publish/check")
    public void checkPublish(@RequestBody AchievementEvent event) {
        publisher.publish(event);
    }
}
