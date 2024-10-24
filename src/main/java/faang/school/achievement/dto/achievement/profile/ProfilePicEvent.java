package faang.school.achievement.dto.achievement.profile;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.achievement.dto.achievement.AbstractEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilePicEvent extends AbstractEvent {

    private String profilePicLink;

    @Override
    @JsonProperty("userId")
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
