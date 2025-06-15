package faang.school.achievement.publisher;

import faang.school.achievement.dto.AnalyticEventDto;

public interface EventPublisher {
    public void publish(AnalyticEventDto message);
}
