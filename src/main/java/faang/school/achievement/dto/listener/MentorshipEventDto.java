package faang.school.achievement.dto.listener;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MentorshipEventDto {

    @Min(1)
    private long mentorId;

    @Min(1)
    private long menteeId;

    @NotNull
    @PastOrPresent
    private LocalDate createdAt;
}
