package faang.school.achievement.dto.userprofile;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class ProfilePicEvent {

    private long userId;
    private String picLink;
    private LocalDateTime timestamp;
}
