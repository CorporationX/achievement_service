package faang.school.achievement.eventhandler;


public interface EventHandler<E> {

    void handle(E event);

    default boolean canBeHandled(E event) {
        return true;
    }
}
