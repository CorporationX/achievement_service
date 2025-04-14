package faang.school.achievement.dto.event;

import faang.school.achievement.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private long eventId;
    private long authorId;
    private EventType eventType;
}
