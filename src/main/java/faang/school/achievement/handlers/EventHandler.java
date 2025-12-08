package faang.school.achievement.handlers;

import faang.school.achievement.exception.HandlerException;

public interface EventHandler<T> {
    void handle(T event) throws HandlerException;
}