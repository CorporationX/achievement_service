package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticEventDto {
    private long receiverId;
    private long actorId;
    private String eventType;
    private String receivedAt;
}
