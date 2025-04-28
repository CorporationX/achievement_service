package faang.school.achievement.sender;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.sender.mock.TestCollectorEventSender;
import faang.school.achievement.sender.mock.TestWriterEventSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SenderTest {
    private final List<AchievementEventSender> achievementEventSenders =
            List.of(new TestWriterEventSender(), new TestCollectorEventSender());
    private final Sender sender = new Sender(achievementEventSenders);

    private final String changedDescription = "TestWriterEventSender";
    private final AchievementEventDto achievementEventDto = new AchievementEventDto();

    @BeforeEach
    void setUp() {
        achievementEventDto.setTitle("WRITER");
        achievementEventDto.setDescription("description");
    }

    @Test
    void send_ShouldSend() {
        sender.send(achievementEventDto);
        assertEquals(changedDescription, achievementEventDto.getDescription());
    }

    @Test
    void send_ShouldExceptionWhenSenderNotFound() {
        achievementEventDto.setTitle("some");
        assertThrows(RuntimeException.class, () -> sender.send(achievementEventDto));
    }

    @Test
    void send_ShouldExceptionWhenNullEvent() {
        assertThrows(NullPointerException.class, () -> sender.send(null));
    }

}