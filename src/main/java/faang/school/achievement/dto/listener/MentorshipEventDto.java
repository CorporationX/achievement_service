package faang.school.achievement.dto.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipEventDto {

    private long mentorId;
    private long menteeId;
    private LocalDate createdAt;
}
