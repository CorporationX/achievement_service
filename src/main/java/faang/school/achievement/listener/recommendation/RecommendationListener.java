package faang.school.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.recommendation.RecommendationEvent;
import faang.school.achievement.eventhandler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationListener extends AbstractEventListener<RecommendationEvent> {
    public RecommendationListener(
            ObjectMapper mapper,
            List<EventHandler<RecommendationEvent>> recommendationEventHandlers
    ) {
        super(
                recommendationEventHandlers,
                mapper,
                RecommendationEvent.class
        );
    }
}
