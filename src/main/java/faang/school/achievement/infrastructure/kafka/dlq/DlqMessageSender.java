package faang.school.achievement.infrastructure.kafka.dlq;

import java.util.Map;
import java.util.Set;

public interface DlqMessageSender {

    /**
     * Отправляет событие в DLQ.
     *
     * @param event            Исходное событие, которое не удалось обработать
     * @param failedHandlers   Имена handlers, которые упали
     * @param retryCount       Количество попыток обработки
     * @param errorMessages    Карта ошибок {handlerName -> errorMessage}
     * @param topicOrTarget    Целевой топик/таблица/очередь (зависит от реализации)
     * @param <T>              Тип события
     */
    <T> void send(
            T event,
            Set<String> failedHandlers,
            long retryCount,
            Map<String, String> errorMessages,
            String eventKey,
            String topicOrTarget
    );
}