package faang.school.achievement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AlbumCreatedEvent extends Event {
    private Long userId;
    private Long albumId;
    private String albumName;

    public AlbumCreatedEvent(LocalDateTime eventTime, Long userId, Long albumId, String albumName) {
        super(eventTime);
        this.userId = userId;
        this.albumId = albumId;
        this.albumName = albumName;
    }
}
