package faang.school.achievement.dto.achievement;

import lombok.Getter;

@Getter
public abstract class AbstractEvent {
    protected long userId;

    public abstract void setUserId(long userId);
}
