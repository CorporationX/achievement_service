package faang.school.achievement.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    HANDLER_NOT_FOUND("Required event handler was not found", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String errorMessage;
    private final HttpStatus httpStatus;
}
