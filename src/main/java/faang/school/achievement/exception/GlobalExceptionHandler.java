package faang.school.achievement.exception;

import faang.school.achievement.dto.ErrorResponse;
import faang.school.achievement.model.Rarity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
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
    public List<ErrorResponse> handleBindException(BindException e) {
        List<ErrorResponse> errors = e.getBindingResult().getFieldErrors().stream()
                .filter(this::isTypeMismatchError)
                .map(this::createErrorResponse)
                .collect(Collectors.toList());

        return errors.isEmpty() ?
                List.of(new ErrorResponse("Invalid Request", "One or more parameters are invalid.")) :
                errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception e) {
        return new ErrorResponse("Internal Server Error", "An unexpected error occurred.");
    }

    private boolean isTypeMismatchError(FieldError fieldError) {
        return fieldError.getCodes() != null &&
                Arrays.stream(fieldError.getCodes())
                        .anyMatch(code -> code != null &&
                                (code.contains("typeMismatch") ||
                                        code.equals("typeMismatch")));
    }

    private ErrorResponse createErrorResponse(FieldError fieldError) {
        if (fieldError.getField().contains("rarity") ||
                fieldError.getDefaultMessage() != null &&
                        fieldError.getDefaultMessage().contains("Rarity")) {

            String allowed = Arrays.stream(Rarity.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            return new ErrorResponse(
                    "Invalid Value",
                    String.format("Invalid value for field '%s'. Allowed values: [%s]",
                            fieldError.getField(), allowed)
            );
        }

        return new ErrorResponse(
                "Invalid Request Parameter",
                String.format("Field '%s' has invalid value", fieldError.getField())
        );
    }
}