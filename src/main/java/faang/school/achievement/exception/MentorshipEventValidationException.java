package faang.school.achievement.exception;

import faang.school.achievement.dto.listener.MentorshipEventDto;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class MentorshipEventValidationException extends Throwable {

    public MentorshipEventValidationException(String validationFailed, Set<ConstraintViolation<MentorshipEventDto>> violations) {
    }
}
