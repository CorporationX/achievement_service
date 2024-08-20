package faang.school.achievement.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
@Slf4j
public class EntityHandler {
    public <T> T getOrThrowException(Class<T> entityClass, Object entityIdentifier, Supplier<Optional<T>> finder) {
        return finder.get().orElseThrow(() -> {
            String errMessage = "Could not find %s by Identifier: %s".formatted(entityClass.getName(), entityIdentifier);
            EntityNotFoundException exception = new EntityNotFoundException(errMessage);
            log.error(errMessage, exception);
            return exception;
        });
    }
}
