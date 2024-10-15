package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.ProfilePicEventMapper;
import faang.school.achievement.protobuf.generate.ProfilePicEventProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilePicEventListenerTest {

    @Mock
    private ProfilePicEventMapper profilePicEventMapper;

    @Mock
    private EventHandlerManager<Event> eventHandlerManager;

    @InjectMocks
    private ProfilePicEventListener profilePicEventListener;

    private byte[] testByteEvent;
    private ProfilePicEventProto.ProfilePicEvent protoEvent;
    private ProfilePicEvent profilePicEvent;

    @BeforeEach
    void setUp() {
        protoEvent = ProfilePicEventProto.ProfilePicEvent.newBuilder().build();
        testByteEvent = protoEvent.toByteArray();
        profilePicEvent = new ProfilePicEvent(LocalDateTime.now(), 123L, "test");
    }

    @Test
    void onMessage_shouldProcessEvent() throws InvalidProtocolBufferException {
        when(profilePicEventMapper.toEvent(protoEvent)).thenReturn(profilePicEvent);

        profilePicEventListener.onMessage(testByteEvent);

        verify(profilePicEventMapper).toEvent(protoEvent);
        verify(eventHandlerManager).processEvent(profilePicEvent);
    }

    @Test
    void onMessage_shouldHandleInvalidProtocolBufferException() {
        assertDoesNotThrow(() -> profilePicEventListener.onMessage(new byte[0]));
    }
}
