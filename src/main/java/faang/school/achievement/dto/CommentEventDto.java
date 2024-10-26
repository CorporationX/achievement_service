package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEventDto {
    private Long commentId;
    private Long commentAuthorId;
    private Long postId;
    private Long postAuthorId;
    private String commentContent;
    private LocalDateTime receivedAt;
}
