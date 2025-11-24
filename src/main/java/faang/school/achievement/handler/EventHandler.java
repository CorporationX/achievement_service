package faang.school.achievement.handler;

import java.util.concurrent.CompletableFuture;

public interface EventHandler<T> {
    CompletableFuture<Void> handle(T event);
}
