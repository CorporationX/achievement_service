package faang.school.achievement.kafka.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class Event {
    private UUID id;
    private LocalDateTime occurredAt;
    private String source;
    private String eventType;
    private Long authorId;
    private Long receiverId;
    private Long userId;
}
