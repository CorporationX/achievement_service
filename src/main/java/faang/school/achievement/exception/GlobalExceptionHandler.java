package faang.school.achievement.exception;

import faang.school.achievement.dto.ErrorResponse;
import faang.school.achievement.model.Rarity;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Map<String, Class<? extends Enum<?>>> enumRegistry = Map.of(
            "Rarity", Rarity.class
    );

    private ErrorResponse buildEnumErrorResponse(String enumName, Class<? extends Enum<?>> enumClass) {
        String validValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        return new ErrorResponse("Invalid %s value. Accepted: %s".formatted(enumName, validValues));
    }

    private ErrorResponse tryHandleEnumConversion(String sourceMessage) {
        return enumRegistry.entrySet().stream()
                .filter(entry -> sourceMessage.contains(entry.getKey()))
                .findFirst()
                .map(entry -> buildEnumErrorResponse(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    // Обработка JSON: enum в теле запроса
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse response = tryHandleEnumConversion(e.getMessage());
        return response != null ? response : new ErrorResponse("Malformed JSON or invalid data: " + e.getMessage());
    }

    // Обработка параметров: enum в query/path
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        if (type != null && type.isEnum()) {
            String simpleName = type.getSimpleName();
            if (enumRegistry.containsKey(simpleName)) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) type;
                return buildEnumErrorResponse(simpleName, enumType);
            }
        }
        return new ErrorResponse("Invalid request parameter: " + e.getMessage());
    }
}