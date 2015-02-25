import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import com.sun.org.apache.bcel.internal.generic.Type;

import sun.misc.Unsafe;

public class UnsafeArray<E> {

	private static Unsafe unsafe = UnsafeUtils.getUnsafe();
	private long baseAddressInMemory;
	private int sizeOfClassInBytes;
	private int length;
	private Class<E> type;

	/**
	 * Creates a new array which objects are stored directly in the array. The
	 * memory of the array is uninitialized.
	 * 
	 * @param type
	 *            The class of objects stored in the array
	 * @param length
	 *            The maximum number of elements. Must be > 0
	 */
	public UnsafeArray(Class<E> type, int length) {
		this.length = length;
		this.type = type;
		sizeOfClassInBytes = UnsafeUtils.sizeof(type);
		baseAddressInMemory = unsafe.allocateMemory(sizeOfClassInBytes * length);
	}

	/**
	 * Gets the element at the specified index.
	 * 
	 * @param index
	 *            The index inside the array. Zero based.
	 * @return The element at the specified index.
	 */
	public E get(int index) {
		long offset = sizeOfClassInBytes * index;
		unsafe.
		//Object obj = unsafe.getObject(ints, offset);
		//return type.cast(obj);
		return null;
		unsafe.
	}

	public void set(E obj, int index) {
		long offset = sizeOfClassInBytes * index;
		long offsetInObject = offset;
		for (Field f : UnsafeUtils.getAllNonStaticFields(type)) {
			try {
				int fieldSize = UnsafeUtils.getFieldSize(f.getClass());
				String datatypeName = uppercaseFirstLetter(f.getType().getName());
				Method method = getAppropriateUnsafeMethod(datatypeName, f);
				invokeMethod(method, datatypeName, offsetInObject, f, obj);
				offsetInObject += fieldSize;
				//Unsafe.class.getMethod(methodName, f.getClass()).invoke(offset, f.getClass().getField(f.getName()));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException | NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private Method getAppropriateUnsafeMethod(String datatypeName, Field f) throws NoSuchMethodException, SecurityException {
		String methodName = "put" + datatypeName;
		if(datatypeName.equals("Boolean"))
			return Unsafe.class.getMethod(methodName, Object.class, long.class, f.getType());
		else
			return Unsafe.class.getMethod(methodName, long.class, f.getType());
	}
	
	private void invokeMethod(Method method, String datatypeName, long offset, Field f, E obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {
		String fieldGetMethodName = "get" + datatypeName;
		Method fieldGetMethod = f.getClass().getMethod(fieldGetMethodName, Object.class);
		if(datatypeName.equals("Boolean"))
			method.invoke(unsafe, baseAddressInMemory, baseAddressInMemory + offset, fieldGetMethod.invoke(f, obj));
		else
			method.invoke(unsafe, baseAddressInMemory + offset, fieldGetMethod.invoke(f, obj));
		
		
		
	}

	/**
	 * Capitalizes the first letter of the input
	 * @param input Must be at least 1 character long
	 * @return A string with the first letter in uppercase
	 */
	private String uppercaseFirstLetter(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

}
