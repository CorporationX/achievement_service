package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.AchievementEventMapper;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEventListenerTest {

    @Mock
    private EventHandlerManager<Event> handlerManager;

    @Mock
    private AchievementEventMapper achievementEventMapper;

    @InjectMocks
    private AchievementEventListener achievementEventListener;

    private AchievementEventProto.AchievementEvent protoEvent;
    private AchievementEvent achievementEvent;

    @BeforeEach
    void setUp() {
        long userId = 1L, achievementId = 2L;
        protoEvent = AchievementEventProto.AchievementEvent.newBuilder()
                .setUserId(userId)
                .setAchievementId(achievementId)
                .build();
        achievementEvent = new AchievementEvent(LocalDateTime.now(), userId, achievementId);
    }

    @Test
    void shouldProcessAchievementEvent() {
        byte[] byteEvent = protoEvent.toByteArray();
        when(achievementEventMapper.toEvent(protoEvent)).thenReturn(achievementEvent);

        achievementEventListener.onMessage(byteEvent);

        verify(achievementEventMapper).toEvent(protoEvent);
        verify(handlerManager).processEvent(achievementEvent);
    }

    @Test
    void shouldLogAndHandleInvalidProtocolBufferException() {
        byte[] invalidByteEvent = new byte[]{1, 2, 3};

        assertDoesNotThrow(() -> achievementEventListener.onMessage(invalidByteEvent));

        verifyNoInteractions(achievementEventMapper, handlerManager);
    }
}
