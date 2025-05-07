package faang.school.achievement.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponseDto {

    private int status;
    private String errorCode;
    private String message;

    @Builder.Default
    private long timestamp = System.currentTimeMillis();
}
