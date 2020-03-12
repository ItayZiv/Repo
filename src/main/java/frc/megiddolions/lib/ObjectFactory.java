package frc.megiddolions.lib;

@FunctionalInterface
public interface ObjectFactory<T> {

    T make();
}
