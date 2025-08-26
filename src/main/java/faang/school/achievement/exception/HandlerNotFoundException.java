package faang.school.achievement.exception;

import faang.school.achievement.dto.ErrorType;
import lombok.Getter;

public class HandlerNotFoundException extends NonRetryableException {
    @Getter
    private ErrorType errorType;

    public HandlerNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }
}
