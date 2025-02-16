package faang.school.achievement.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamEvent {
    private Long teamId;
    private Long authorId;
    private Long projectId;
    private LocalDateTime localDateTime;

}
