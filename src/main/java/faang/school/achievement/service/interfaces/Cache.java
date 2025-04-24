package faang.school.achievement.service.interfaces;

import java.util.List;

public interface Cache<T> {
    T get(String title);

    List<T> getAll();
}
