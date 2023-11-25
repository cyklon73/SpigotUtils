package de.cyklon.spigotutils.nms;

import org.bukkit.Bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NMSReflection {

	private static final String NM_PACKAGE = "net.minecraft";
	private static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
	private static final String NMS_PACKAGE = NM_PACKAGE + ".server";

	private static final String VERSION_NAME = Bukkit.getServer().getClass().getPackage().getName().substring(OBC_PACKAGE.length() + 1);

	private static final MethodType VOID_METHOD_TYPE = MethodType.methodType(void.class);
	private static final boolean NMS_REPACKAGED = optionalClass(NM_PACKAGE + ".network.protocol.Packet").isPresent();

	private static volatile Object theUnsafe;

	@SuppressWarnings("unchecked")
	public static <D, R> R invokeMethod(D obj, String methodName, Class<R> returnType, Class<?>[] paramTypes, Object... params) {
		List<Class<?>> types = new ArrayList<>();
		if (paramTypes==null) paramTypes = new Class<?>[0];
		for (int i = 0; i < params.length; i++) {
			if (i<paramTypes.length && paramTypes[i]!=null) types.add(paramTypes[i]);
			else types.add(params[i].getClass());
		}
		ReflectMethod<D, R> method = getMethod((Class<D>) obj.getClass(), methodName, returnType, types.toArray(Class[]::new));
		return invokeMethod(method, obj, params);
	}

	public static <D, R> R invokeStaticMethod(Class<D> clazz, String methodName, Class<R> returnType, Class<?>[] paramTypes, Object... params) {
		List<Class<?>> types = new ArrayList<>();
		if (paramTypes==null) paramTypes = new Class<?>[0];
		for (int i = 0; i < params.length; i++) {
			if (i<paramTypes.length && paramTypes[i]!=null) types.add(paramTypes[i]);
			else types.add(params[i].getClass());
		}
		ReflectMethod<D, R> method = getMethod(clazz, methodName, returnType, types.toArray(Class[]::new));
		return invokeStaticMethod(method, params);
	}

	public static <D, R> R invokeMethod(ReflectMethod<D, R> method, D obj, Object... params) {
		return invokeMethodWithInconstantClass(method, obj, params);
	}

	public static <R> R invokeMethodWithInconstantClass(ReflectMethod<?, R> method, Object obj, Object... params) {
		return method.getReturnType().cast(invokeInconstantMethod(method, obj, params));
	}


	public static Object invokeInconstantMethod(ReflectMethod<?, ?> method, Object obj, Object... params) {
		try {
			return method.getMethod().invoke(obj, params);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static <D, R> R invokeStaticMethod(ReflectMethod<D, R> method, Object... params) {
		return invokeMethod(method, null, params);
	}

	public static <D, R> ReflectMethod<D, R> getMethod(final Class<D> declaringClass, String methodName, final Class<R> returnType, Class<?>... paramTypes) {
		try {
			final Method method = declaringClass.getDeclaredMethod(methodName, paramTypes);
			method.setAccessible(true);
			return new ReflectMethod<>() {
				@Override
				public Class<R> getReturnType() {
					return returnType;
				}

				@Override
				public Method getMethod() {
					return method;
				}

				@Override
				public Class<D> getDeclaringClass() {
					return declaringClass;
				}
			};
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static <R> R getStaticValueWithInconstantClass(ReflectField<?, R> field) {
		return getValueWithInconstantClass(field, null);
	}

	public static <R> R getValueWithInconstantClass(ReflectField<?, R> field, Object obj) {
		return field.getReturnType().cast(getInconstantValue(field, obj));
	}

	public static Object getStaticInconstantValue(ReflectField<?, ?> field) {
		return getInconstantValue(field, null);
	}

	public static Object getInconstantValue(ReflectField<?, ?> field, Object obj) {
		try {
			return field.getReturnType().cast(field.getField().get(obj));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <D, R> R getStaticValue(ReflectField<D, R> field) {
		return getValue(field, null);
	}

	public static <D, R> R getValue(ReflectField<D, R> field, Object obj) {
		return field.getReturnType().cast(getInconstantValue(field, obj));
	}

	public static <D, R> ReflectField<D, R> getField(final Class<D> declaringClass, String fieldName, final Class<R> returnType) {
		try {
			final Field field = declaringClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return new ReflectField<>() {
				@Override
				public Field getField() {
					return field;
				}

				@Override
				public Class<R> getReturnType() {
					return returnType;
				}

				@Override
				public Class<D> getDeclaringClass() {
					return declaringClass;
				}
			};
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz, Object... params) {
		return newInstance(clazz, null, params);
	}

	public static <T> T newInstance(Class<T> clazz, Class<?>[] paramTypes, Object... params) {
		List<Class<?>> types = new ArrayList<>();
		if (paramTypes==null) paramTypes = new Class<?>[0];
		for (int i = 0; i < params.length; i++) {
			if (i<paramTypes.length && paramTypes[i]!=null) types.add(paramTypes[i]);
			else types.add(params[i].getClass());
		}
		try {
			return getConstructor(clazz, types.toArray(Class[]::new)).newInstance(params);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... params) {
		try {
			return clazz.getConstructor(params);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException();
		}
	}

	public static String nmsClassName(String post1_17package, String className) {
		if (NMS_REPACKAGED) {
			String classPackage = post1_17package == null ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
			return classPackage + '.' + className;
		}
		return NMS_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	public static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
		return Class.forName(nmsClassName(post1_17package, className));
	}

	public static Optional<Class<?>> nmsOptionalClass(String post1_17package, String className) {
		return optionalClass(nmsClassName(post1_17package, className));
	}

	public static String obcClassName(String className) {
		return OBC_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	public static Class<?> obcClass(String className) throws ClassNotFoundException {
		return Class.forName(obcClassName(className));
	}

	public static Optional<Class<?>> optionalClass(String className) {
		try {
			return Optional.of(Class.forName(className));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	}

	public static Object enumValueOf(Class<?> enumClass, String enumName) {
		return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
	}

	public static Object enumValueOf(Class<?> enumClass, String enumName, int fallbackOrdinal) {
		try {
			return enumValueOf(enumClass, enumName);
		} catch (IllegalArgumentException e) {
			Object[] constants = enumClass.getEnumConstants();
			if (constants.length > fallbackOrdinal) {
				return constants[fallbackOrdinal];
			}
			throw e;
		}
	}

	public static Class<?> innerClass(Class<?> parentClass, Predicate<Class<?>> classPredicate) throws ClassNotFoundException {
		for (Class<?> innerClass : parentClass.getDeclaredClasses()) {
			if (classPredicate.test(innerClass)) {
				return innerClass;
			}
		}
		throw new ClassNotFoundException("No class in " + parentClass.getCanonicalName() + " matches the predicate.");
	}

	public static PacketConstructor findPacketConstructor(Class<?> packetClass, MethodHandles.Lookup lookup) throws Exception {
		try {
			MethodHandle constructor = lookup.findConstructor(packetClass, VOID_METHOD_TYPE);
			return constructor::invoke;
		} catch (NoSuchMethodException | IllegalAccessException e) {
			// try below with Unsafe
		}

		if (theUnsafe == null) {
			synchronized (NMSReflection.class) {
				if (theUnsafe == null) {
					Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
					Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
					theUnsafeField.setAccessible(true);
					theUnsafe = theUnsafeField.get(null);
				}
			}
		}

		MethodType allocateMethodType = MethodType.methodType(Object.class, Class.class);
		MethodHandle allocateMethod = lookup.findVirtual(theUnsafe.getClass(), "allocateInstance", allocateMethodType);
		return () -> allocateMethod.invoke(theUnsafe, packetClass);
	}

}
