package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementFilterDto {

    @Size(max = 128)
    private String title;

    @Size(max = 1024)
    private String description;

    private Rarity rarity;

    private Integer page;

    private Integer size;

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }
}
