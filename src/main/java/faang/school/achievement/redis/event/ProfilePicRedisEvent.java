package faang.school.achievement.redis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicRedisEvent {
    private Long userId;
    private String picKey;
}
