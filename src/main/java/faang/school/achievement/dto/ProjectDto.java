package faang.school.achievement.dto;

import faang.school.achievement.model.ProjectStatus;
import faang.school.achievement.model.ProjectVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private BigInteger storageSize;
    private BigInteger maxStorageSize;
    private Long ownerId;
    private Long parentProjectId;
    private List<Long> childrenId;
    private List<Long> tasksId;
    private List<Long> resourcesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProjectVisibility visibility;
    private ProjectStatus status;
    private String coverImageId;
    private List<Long> teamsId;
    private Long scheduleId;
    private List<Long> stagesId;
    private List<Long> vacanciesId;
}
