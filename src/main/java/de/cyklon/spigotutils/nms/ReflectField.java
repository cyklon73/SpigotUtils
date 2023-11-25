package de.cyklon.spigotutils.nms;

import java.lang.reflect.Field;

public interface ReflectField<D, R> extends ReflectEntity<D, R> {

    Field getField();

}
