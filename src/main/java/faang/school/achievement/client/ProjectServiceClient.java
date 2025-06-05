package faang.school.achievement.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${services.project-service.name}")
public interface ProjectServiceClient {

}
