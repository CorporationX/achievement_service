package faang.school.achievement.mapper;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateTimeMapperTest {

    private final DateTimeMapper dateTimeMapper = new DateTimeMapper() {};

    @Test
    void toLocalDateTime_shouldConvertProtobufTimestampToLocalDateTime() {
        Instant instant = Instant.now();
        Timestamp protobufTimestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
        LocalDateTime expectedLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        LocalDateTime actualLocalDateTime = dateTimeMapper.toLocalDateTime(protobufTimestamp);

        assertNotNull(actualLocalDateTime);
        assertEquals(expectedLocalDateTime, actualLocalDateTime);
    }

    @Test
    void toLocalDateTime_shouldReturnNullWhenTimestampIsNull() {
        LocalDateTime localDateTime = dateTimeMapper.toLocalDateTime(null);

        assertNull(localDateTime);
    }
}

