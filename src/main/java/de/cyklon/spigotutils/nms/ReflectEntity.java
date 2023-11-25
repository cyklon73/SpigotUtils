package de.cyklon.spigotutils.nms;

public interface ReflectEntity<D, R> {

    Class<R> getReturnType();

    Class<D> getDeclaringClass();
}
