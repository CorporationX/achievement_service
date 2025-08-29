package faang.school.achievement.exception;

import org.slf4j.helpers.MessageFormatter;

public class JsonConvertException extends NonRetryableException {

    public JsonConvertException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }
}
