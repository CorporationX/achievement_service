package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.publisher.AchievementEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event/achievement")
public class AchievementEventPublisherTestController {
    private final AchievementEventPublisher publisher;

    @PostMapping("/post")
    public void sendEvent(@RequestBody AchievementEvent event){
        publisher.sendMessage(event);
    }
}
