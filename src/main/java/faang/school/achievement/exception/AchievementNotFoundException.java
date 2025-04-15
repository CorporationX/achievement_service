package faang.school.achievement.exception;

public class AchievementNotFoundException extends CustomException {

    public AchievementNotFoundException(ExceptionMessage message, long id) {
        super(message, id);
    }
}
