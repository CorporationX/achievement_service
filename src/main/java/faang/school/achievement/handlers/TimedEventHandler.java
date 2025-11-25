package faang.school.achievement.handlers;

public interface TimedEventHandler<T> extends EventHandler<T> {
    long getHandlerExecutionTime();
}