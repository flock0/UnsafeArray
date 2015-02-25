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

	private final static Unsafe unsafe = UnsafeUtils.getUnsafe();
	private final long baseAddressInMemory;
	private final int sizeOfClassInBytes;
	private final int length;
	private final Class<E> type;

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
		return null;
	}

	/**
	 * Writes an object to the array.
	 * 
	 * As the object provided will be copied to the array, you should use the object returned by the method. 
	 * @param obj The object to write to the array
	 * @param index The index inside the array. Zero based.
	 * @return The element that has been stored in the array.
	 */
	public E set(E obj, int index) {
		long offsetFromBaseAddress = sizeOfClassInBytes * index;
		long sourceAddress = UnsafeUtils.getAddressOf(obj);
		long destAddress = baseAddressInMemory + offsetFromBaseAddress;
		unsafe.copyMemory(sourceAddress, destAddress, sizeOfClassInBytes);
		
		Object[] helperArray = new Object[1];
		long helperBaseOffset = unsafe.arrayBaseOffset(Object[].class);
		long addressOfFirstHelperElement = UnsafeUtils.getAddressOf(helperArray) + helperBaseOffset;
		unsafe.putLong(addressOfFirstHelperElement, destAddress);
		
		return (E) helperArray[0];
		
	}

	private boolean bytesAreEqual(long srcAddress, long destAddress, long sizeOfClassInBytes) {
		for(int i = 0; i < sizeOfClassInBytes; i++) {
			if(unsafe.getByte(srcAddress + i) != unsafe.getByte(destAddress + i))
				return false;
		}
		return true;
	}
}
