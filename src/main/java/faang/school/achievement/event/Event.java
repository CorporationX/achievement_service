package faang.school.achievement.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class Event {
    @JsonProperty("eventId")
    private String eventId = UUID.randomUUID().toString();

    @JsonProperty("timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("eventType")
    private EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }
}
