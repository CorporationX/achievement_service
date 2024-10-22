package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.MentorshipStartEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.handler.SenseyAchievementHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipStartEventListenerTest {

    @InjectMocks
    private MentorshipStartEventListener mentorshipStartEventListener;

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SenseyAchievementHandler senseyAchievementHandler;

    private List<EventHandler<MentorshipStartEvent>> eventHandlers;
    private MentorshipStartEvent mentorshipStartEvent;
    private static final long ID = 1L;

    @BeforeEach
    public void init() {
        eventHandlers = new ArrayList<>();
        mentorshipStartEventListener = new MentorshipStartEventListener(objectMapper, eventHandlers);
        mentorshipStartEvent = MentorshipStartEvent.builder()
                .mentorId(ID)
                .menteeId(ID)
                .build();
    }

    @Test
    @DisplayName("Успешная отправка event в SenseyAchievementHandler")
    public void whenOnMessageWithCurrentDataThenSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipStartEvent.class)).thenReturn(mentorshipStartEvent);
        eventHandlers.add(senseyAchievementHandler);

        mentorshipStartEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), MentorshipStartEvent.class);
        verify(eventHandlers.get(0)).handleEvent(mentorshipStartEvent);
    }
}