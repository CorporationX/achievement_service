package faang.school.achievement.infrastructure.kafka.dlq;

import faang.school.achievement.dto.DlqMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Сервис для отправки failed событий в Dead Letter Queue.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDlqMessageSender implements DlqMessageSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Отправляет событие в DLQ со всеми метаданными о failed обработке.
     *
     * @param event          Исходное событие
     * @param failedHandlers Имена handlers, которые упали
     * @param retryCount     Количество попыток обработки
     * @param errorMessages  Карта ошибок {handler -> error}
     * @param dlqTopicName   Название DLQ топика
     */
    public <T> void send(
            T event,
            Set<String> failedHandlers,
            long retryCount,
            Map<String, String> errorMessages,
            String eventKey,
            String dlqTopicName
    ) {
        DlqMessageDto<T> dlqMessage = new DlqMessageDto<>(
                event,
                new ArrayList<>(failedHandlers),
                retryCount,
                errorMessages,
                eventKey,
                LocalDateTime.now()
        );

        log.error(
                "Sending event {} to DLQ after {} failed attempts. "
                        + "Failed handlers: {}. Topic: {}, eventKey: {}",
                dlqMessage.eventKey(),
                retryCount,
                failedHandlers,
                dlqTopicName,
                eventKey
        );

        kafkaTemplate.send(dlqTopicName, dlqMessage.eventKey(), dlqMessage);
    }
}