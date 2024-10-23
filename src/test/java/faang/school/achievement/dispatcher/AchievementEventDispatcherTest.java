package faang.school.achievement.dispatcher;

import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.event.AchievementEventDispatcher;
import faang.school.achievement.handler.AchievementHandler;
import faang.school.achievement.handler.BusinessmanAchievementHandler;
import faang.school.achievement.handler.BusinessmanAchievementHandlerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementEventDispatcherTest {
    @InjectMocks
    private AchievementEventDispatcher<ProjectEvent> dispatcher;
    @Mock
    private List<AchievementHandler<ProjectEvent>> handlers;
    @Mock
    private BusinessmanAchievementHandler handler;
    private ProjectEvent event;


    @BeforeEach
    void setUp() {
        event = new ProjectEvent();
    }
    @Test
    void dispatchEvent() {
        List<AchievementHandler<ProjectEvent>> handlers = new ArrayList<>();
        handlers.add(handler);
        dispatcher = new AchievementEventDispatcher<>(handlers);

        dispatcher.dispatchEvent(event);

        verify(handler, times(1)).handleAchievement(event);
    }
}
