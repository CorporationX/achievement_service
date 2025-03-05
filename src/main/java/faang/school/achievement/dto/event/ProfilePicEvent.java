package faang.school.achievement.dto.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class ProfilePicEvent extends ApplicationEvent {
    private Long userId;
    private String profilePicUrl;

    public ProfilePicEvent(Object source, Long userId, String profilePicUrl) {
        super(source);
        this.userId = userId;
        this.profilePicUrl = profilePicUrl;
    }
}
