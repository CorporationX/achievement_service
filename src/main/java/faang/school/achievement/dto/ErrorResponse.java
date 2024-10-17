package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String serviceName;
    private int errorCode;
    private String globalMessage;
    private Map<String, String> fieldMessages;
}
