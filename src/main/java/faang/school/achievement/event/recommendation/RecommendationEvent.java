package faang.school.achievement.event.recommendation;

import faang.school.achievement.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecommendationEvent implements Event {
    private Long recommenderId;
    private Long recommendedId;
    private String recommendationText;
}
