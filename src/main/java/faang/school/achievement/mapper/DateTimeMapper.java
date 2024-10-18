package faang.school.achievement.mapper;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public interface DateTimeMapper {

    default LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return Optional.ofNullable(timestamp).map(ts -> {
            Instant instant = Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }).orElse(null);
    }

    default Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Timestamp.newBuilder()
                .setSeconds(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond())
                .setNanos(localDateTime.getNano())
                .build();
    }
}
