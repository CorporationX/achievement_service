package faang.school.achievement.sender;

import faang.school.achievement.dto.event.AchievementEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Sender {
    private static final String SENDER_NOT_FOUND_MSG = "achievement event sender not found for ";
    private final List<AchievementEventSender> achievementEventSenders;

    public void send(AchievementEventDto achievementEventDto) {
        achievementEventSenders.stream()
                .filter(sender -> sender.getTitle().equals(achievementEventDto.getTitle()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(SENDER_NOT_FOUND_MSG + achievementEventDto))
                .send(achievementEventDto);
    }
}