import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import sun.misc.Unsafe;


public class UnsafeUtils {
	
	private static Unsafe unsafe;
	
	static {
		try {
			Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
			unsafeConstructor.setAccessible(true);
			unsafe = unsafeConstructor.newInstance();
		} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the size of the given class in bytes.
	 * This is the sum of bytes allocated for the fields.
	 * 
	 * @param cls The class whose size to get. Must not be null or an array
	 * @return The size of the class in bytes
	 */
	public static int sizeof(Class<?> cls) {
		int sum = 0;
		for (Field f : getAllNonStaticFields(cls))
			sum += getFieldSize(f.getClass());
		return sum;
    }
	
	/**
	 * Returns all non-static fields of a class (including those from its superclasses)
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
	 * @param cls The class whose size to get
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
            return 4; // Reference pointers are assumed to be 4 byte long
	}
	
	private static int modulo8(int value) {
        return (value & 0x7) > 0 ? (value & ~0x7) + 8 : value;
    }

	public static Unsafe getUnsafe() {
		return unsafe;
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
