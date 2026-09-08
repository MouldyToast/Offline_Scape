package com.zenyte.game.task;

@FunctionalInterface
public interface WorldTask extends Runnable {

    default void stop() {
        WorldTasksManager.stop(this);
    }

    default boolean exists() {
        return WorldTasksManager.containsTask(this);
    }

    default void printCreationStackTrace() {
        Thread.dumpStack();
    }

}
