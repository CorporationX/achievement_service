package faang.school.achievement.service;

import faang.school.achievement.client.UserServiceClient;
import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.client.UserDto;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.exception.EntityNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public UserDto getUser(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException.FeignClientException ex) {
            throw new EntityNotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
    }

    public boolean isUserExists(long userId) {
        try {
            userServiceClient.getUser(userId);
            return true;
        } catch (FeignException.FeignClientException ex) {
            return false;
        }
    }

    public void checkUser(long userId) {
        if (userContext.getUserId() != userId) {
            throw new DataValidationException("Id пользователя не совпадает с Id владельца");
        }
    }
}
