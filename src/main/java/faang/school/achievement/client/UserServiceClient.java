package faang.school.achievement.client;

import faang.school.achievement.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/users/api")
public interface UserServiceClient {
    @GetMapping("/v1/users/{id}")
    UserDto getUserById(@PathVariable Long id);
}
