package faang.school.achievement.service;

import faang.school.achievement.client.UserServiceClient;
import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.user.UserDto;
import faang.school.achievement.exception.EntityNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public UserDto getUserById(Long id, Long userContextId) {
        try {
            userContext.setUserId(userContextId);
            return userServiceClient.getUserById(id);
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException("Пользователь с id " + id + " не найден");
        }
    }
}
