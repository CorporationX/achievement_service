package faang.school.achievement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    EMPTY_FILTER("At least one filter parameter must be provided"),
    ;

    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
