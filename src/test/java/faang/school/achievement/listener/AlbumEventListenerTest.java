package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.AlbumEventMapper;
import faang.school.achievement.protobuf.generate.AlbumCreatedEventProto;
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
class AlbumEventListenerTest {

    @Mock
    private AlbumEventMapper albumEventMapper;

    @Mock
    private EventHandlerManager<Event> eventHandlerManager;

    @InjectMocks
    private AlbumEventListener AlbumEventListener;

    private byte[] testByteEvent;
    private AlbumCreatedEventProto.AlbumCreatedEvent protoEvent;
    private AlbumCreatedEvent followerEvent;

    @BeforeEach
    void setUp() {
        protoEvent = AlbumCreatedEventProto.AlbumCreatedEvent.newBuilder().build();
        testByteEvent = protoEvent.toByteArray();
        followerEvent = new AlbumCreatedEvent(LocalDateTime.now(), 1L, 2L, "albumName");
    }

    @Test
    void onMessage_shouldProcessEvent() throws InvalidProtocolBufferException {
        when(albumEventMapper.toAlbumCreatedEvent(protoEvent)).thenReturn(followerEvent);

        AlbumEventListener.onMessage(testByteEvent);

        verify(albumEventMapper).toAlbumCreatedEvent(protoEvent);
        verify(eventHandlerManager).processEvent(followerEvent);
    }

    @Test
    void onMessage_shouldHandleInvalidProtocolBufferException() {
        assertDoesNotThrow(() -> AlbumEventListener.onMessage(new byte[0]));
    }
}
