package faang.school.achievement.exception;

import faang.school.achievement.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException e) {
        return new ErrorResponse("Entity Not Found", e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(BindException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> fieldError.getCodes() != null)
                .filter(fieldError -> Arrays.stream(fieldError.getCodes())
                        .anyMatch(code -> code.contains("typeMismatch")))
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .filter(fieldError -> fieldError.getDefaultMessage().contains("Failed to convert"))
                .map(fieldError -> {
                    // Пытаемся извлечь имя enum из сообщения
                    String rawMessage = fieldError.getDefaultMessage();
                    if (rawMessage.contains("Rarity")) {
                        String allowed = Arrays.stream(faang.school.achievement.model.Rarity.values())
                                .map(Enum::name)
                                .collect(Collectors.joining(", "));
                        return new ErrorResponse(
                                "Invalid Value",
                                "Invalid value for Rarity. Allowed values are: [" + allowed + "]"
                        );
                    }
                    return new ErrorResponse(
                            "Invalid Request Parameter",
                            "One or more parameters have invalid values."
                    );
                })
                .findFirst()
                .orElseGet(() -> new ErrorResponse(
                        "Invalid Request",
                        "One or more parameters are invalid."
                ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception e) {
        return new ErrorResponse("Internal Server Error", "An unexpected error occurred.");
    }
}