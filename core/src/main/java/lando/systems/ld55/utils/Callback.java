package lando.systems.ld55.utils;

@FunctionalInterface
public interface Callback {
    void run(Object... params);
}
