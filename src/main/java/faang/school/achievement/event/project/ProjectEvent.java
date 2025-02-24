package faang.school.achievement.event.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.achievement.event.Event;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectEvent implements Event {
    private long projectId;
    private long userId;
    private LocalDateTime createAt;

    @Override
    public long getUserId() {
        return userId;
    }
}
