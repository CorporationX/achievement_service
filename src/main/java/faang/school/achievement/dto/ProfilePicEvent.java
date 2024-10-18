package faang.school.achievement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProfilePicEvent extends Event {
    private Long userId;
    private String profilePic;

    public ProfilePicEvent(LocalDateTime eventTime, Long userId, String profilePic) {
        super(eventTime);
        this.userId = userId;
        this.profilePic = profilePic;
    }
}
