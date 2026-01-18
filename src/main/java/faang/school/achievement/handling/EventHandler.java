package faang.school.achievement.handling;
/**
 * Создан для всех классов являющихся абстрактными обработчиками достижений(простых) по событию
 * Помогает задать контракт для всех абстрактных обработчиков
 */
public interface EventHandler<T> {
    void handleEvent(T event);
}