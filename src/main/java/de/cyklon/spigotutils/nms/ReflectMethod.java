package de.cyklon.spigotutils.nms;

import java.lang.reflect.Method;

public interface ReflectMethod<D, R> extends ReflectEntity<D, R> {
    Method getMethod();
}
