package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.postEvent.PostEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PostEventListenerTest {
    private static final String MESSAGE_ERROR = "ReadValue exception";
    private static final long FIRST_ID = 1L;
    private final String STRING = "\"authorId\":1,\"postId\":2";
    private PostEvent event;
    private Message message;
    @Mock
    private List<EventHandler<PostEvent>> eventHandlers;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private PostEventListener listener;

    @BeforeEach
    void setUp() {
        //Arrange
        message = new Message() {
            @Override
            public byte[] getBody() {
                return STRING.getBytes();
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        };
        event = PostEvent.builder()
                .authorId(FIRST_ID)
                .postId(FIRST_ID)
                .build();
    }

    @Test
    void testOnMessageValid() throws IOException {
        //Act
        Mockito.when(objectMapper.readValue(message.getBody(), PostEvent.class)).thenReturn(event);
        listener.onMessage(message, new byte[]{});
        //Assert
        Mockito.verify(eventHandlers).forEach(Mockito.any());
    }

    @Test
    void testOnMessageInvalid() throws IOException {
        //Act
        Mockito.when(objectMapper.readValue(message.getBody(), PostEvent.class))
                .thenThrow(new RuntimeException(MESSAGE_ERROR));
        //Assert
        assertEquals(MESSAGE_ERROR,
                assertThrows(RuntimeException.class,
                        () -> listener.onMessage(message, new byte[]{})).getMessage());
    }
}
