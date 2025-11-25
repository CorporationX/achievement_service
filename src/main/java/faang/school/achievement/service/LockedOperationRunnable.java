package faang.school.achievement.service;

@FunctionalInterface
public interface LockedOperationRunnable {
    void run() throws Exception;
}