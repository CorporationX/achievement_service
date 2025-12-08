package faang.school.achievement.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record DlqMessageDto<T>(
        T originalEvent,
        List<String> failedHandlers,
        long retryCount,
        Map<String, String> errorMessages,
        String eventKey,
        LocalDateTime dlqTimestamp
) {
}