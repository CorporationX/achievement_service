package faang.school.achievement.service;

@FunctionalInterface
public interface LockedOperationSupplier<T> {
    T get() throws Exception;
}