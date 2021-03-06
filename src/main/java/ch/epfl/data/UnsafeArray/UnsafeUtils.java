package ch.epfl.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import sun.misc.Unsafe;

/**
 * Contains some helper methods for dealing with unsafe operations.
 */
public class UnsafeUtils {

    private static final int NR_BITS = Integer.valueOf(System.getProperty("sun.arch.data.model"));
    private static final int BYTE = 8;
    private static final int WORD = NR_BITS / BYTE;

    private static Unsafe unsafe;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class
                    .getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            unsafe = unsafeConstructor.newInstance();
        } catch (InvocationTargetException | IllegalArgumentException
                | IllegalAccessException | InstantiationException
                | NoSuchMethodException | SecurityException e) {
            /* Nothing we can do about it */
            e.printStackTrace();
        }
    }

    /**
     * Gets the address of an object in memory
     * 
     * @param o
     *            The object
     * @return The address of the object in memory
     */
    public static long getAddressOf(Object o) {

        Object[] array = new Object[] { o };

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        int addressSize = unsafe.addressSize();
        long objectAddress;
        switch (addressSize) {
            case 4:
                objectAddress = unsafe.getInt(array, baseOffset);
                break;
            case 8:
                objectAddress = unsafe.getLong(array, baseOffset);
                break;
            default:
                throw new Error("unsupported address size: " + addressSize);
        }

        return (objectAddress);
    }

    /**
     * Returns all non-static fields of a class (including those from its
     * superclasses)
     */
    public static Field[] getAllNonStaticFields(Class<?> cls) {
        if (cls == null)
            throw new NullPointerException();

        List<Field> fieldList = new ArrayList<Field>();
        while (cls != Object.class) {
            for (Field f : sortFieldsByName(cls.getDeclaredFields())) {
                if (!Modifier.isStatic(f.getModifiers()))
                    fieldList.add(f);
            }
            cls = cls.getSuperclass();
        }
        Field[] fs = new Field[fieldList.size()];
        fieldList.toArray(fs);
        return fs;
    }

    /**
     * Returns the amount of memory allocated to the class in bytes
     * 
     * @param cls
     *            The class whose size to get
     * @return The size in bytes
     */
    public static int getFieldSize(Class<?> cls) {
        if (cls == byte.class)
            return 1;
        if (cls == boolean.class)
            return 1;
        if (cls == char.class)
            return 2;
        if (cls == short.class)
            return 2;
        if (cls == int.class)
            return 4;
        if (cls == float.class)
            return 4;
        if (cls == long.class)
            return 8;
        if (cls == double.class)
            return 8;
        else
            /* Reference pointers are assumed to be 4 byte (x86) or 8 byte (x64) long */
            return WORD;
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    private static int modulo8(int value) {
        return (value & 0x7) > 0 ? (value & ~0x7) + 8 : value;
    }

    /**
     * Returns the size of the given class in bytes
     * 
     * @param cls
     *            The class whose size to get. Must not be null or an array
     * 
     * @see https
     *      ://stackoverflow.com/questions/8775865/is-there-a-simple-way-to-
     *      get-the-size-of-a-java-object
     */
    public static int sizeof(Class<?> cls) {

        if (cls == null)
            throw new NullPointerException();

        if (cls.isArray())
            throw new IllegalArgumentException();

        if (cls.isPrimitive())
            return getFieldSize(cls);

        int lastOffset = Integer.MIN_VALUE;
        Class<?> lastClass = null;

        for (Field f : getAllNonStaticFields(cls)) {
            int offset = (int) unsafe.objectFieldOffset(f);
            if (offset > lastOffset) {
                lastOffset = offset;
                lastClass = f.getClass();
            }
        }
        if (lastOffset > 0)
            return modulo8(lastOffset + getFieldSize(lastClass));
        else
            return 2 * WORD;
    }

    private static Field[] sortFieldsByName(Field[] fields) {
        Arrays.sort(fields, new FieldNameComparator());
        return fields;
    }

    private static class FieldNameComparator implements Comparator<Field> {
        @Override
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
