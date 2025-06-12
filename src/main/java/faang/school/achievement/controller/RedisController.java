package faang.school.achievement.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import faang.school.achievement.publisher.AchievmentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redismessage")
public class RedisController {
    private final AchievmentEventPublisher achievmentEventPublisher;

    @PostMapping()
    public void publishMessage(@RequestBody String message) {
        log.info("Mesage received to controlelr {}.", message);
        achievmentEventPublisher.publish(message);
    }
}
