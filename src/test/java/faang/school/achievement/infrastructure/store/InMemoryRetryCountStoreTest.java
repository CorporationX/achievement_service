package faang.school.achievement.infrastructure.store;

import faang.school.achievement.handlers.TimedEventHandler;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class InMemoryRetryCountStoreTest {

    private InMemoryRetryCountStore<Object> store;
    private final String eventKey = "event-123";

    @BeforeEach
    void setUp() {
        store = new InMemoryRetryCountStore<>();
    }

    @Test
    void incrementRetryCount_ShouldStartAtOne() {
        store.incrementRetryCount(eventKey);

        assertEquals(1, store.getRetryCount(eventKey));
    }

    @Test
    void incrementRetryCount_ShouldIncrementExistingValue() {
        store.incrementRetryCount(eventKey);
        store.incrementRetryCount(eventKey);
        store.incrementRetryCount(eventKey);

        assertEquals(3, store.getRetryCount(eventKey));
    }

    @Test
    void getRetryCount_ShouldReturnZeroForUnknownKey() {
        int count = store.getRetryCount("unknown-key");

        assertEquals(0, count);
    }

    @Test
    void setAndGetUnworkedHandlers_ShouldStoreAndRetrieveList() {
        TimedEventHandler<Object> handler1 = mock(TimedEventHandler.class);
        TimedEventHandler<Object> handler2 = mock(TimedEventHandler.class);
        List<TimedEventHandler<Object>> handlers = List.of(handler1, handler2);

        store.setUnworkedHandlers(eventKey, handlers);

        List<TimedEventHandler<Object>> retrievedHandlers = store.getUnworkedHandlers(eventKey);

        assertEquals(handlers, retrievedHandlers);
        assertEquals(2, retrievedHandlers.size());
    }

    @Test
    void clearRetryState_ShouldRemoveCountAndHandlers() {
        store.incrementRetryCount(eventKey);
        store.setUnworkedHandlers(eventKey, List.of(mock(TimedEventHandler.class)));

        assertEquals(1, store.getRetryCount(eventKey));
        assertEquals(1, store.getUnworkedHandlers(eventKey).size());

        store.clearRetryState(eventKey);

        assertEquals(0, store.getRetryCount(eventKey));
        assertNull(store.getUnworkedHandlers(eventKey));
    }
}