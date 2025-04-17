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
    private String eventType;

    public Event(String eventType) {
        this.eventType = eventType;
    }
}
