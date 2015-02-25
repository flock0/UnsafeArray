import java.util.Arrays;

import sun.misc.Unsafe;

/**
 * An array implementation that saves objects directly in the memory area of the array.
 *   
 * In difference to the default way arrays are implemented in Java, we don't use an array of references, but store the objects directly in the array fields.
 * When storing an object in the array, the object is copied to the array. Thus, one should not use the initial object, but the object returned by the copyAndSet-method.
 * 
 * Right now, this implementation is only compatible with classes that do not include any fields of a Reference type inside the class itself. 
 * 
 * @author Florian Chlan
 *
 * @param <E> The type of objects stored in the array.
 */
public class UnsafeArray<E> {

	private final static Unsafe unsafe = UnsafeUtils.getUnsafe();
	private final long baseAddressInMemory;
	private final int sizeOfClassInBytes;
	private final int length;
	boolean[] initialized;

	/**
	 * Creates a new array which objects are stored directly in the array.
	 * 
	 * @param type
	 *            The class of objects stored in the array (as defined in the type parameter
	 * @param length
	 *            The maximum number of elements. Must be > 0
	 */
	public UnsafeArray(Class<E> type, int length) {
		if(length < 1)
			throw new IllegalArgumentException("Arraylength is <= 0.");
		this.length = length;
		sizeOfClassInBytes = UnsafeUtils.sizeof(type);
		baseAddressInMemory = unsafe.allocateMemory(sizeOfClassInBytes * length);
		initialized = new boolean[length];
		Arrays.fill(initialized, false);
	}

	/**
	 * Gets the element at the specified index.
	 * 
	 * @param index The index inside the array. Zero based.
	 * @return The element at the specified index.
	 * @throws NotYetInitializedException When no object has been set at the field with the provided index
	 */
	public E get(int index) throws NotYetInitializedException {
		if(index < 0 || index >= length)
			throw new ArrayIndexOutOfBoundsException(String.format("Tried to access element at index %d.", index));
		if(initialized[index] == false)
			throw new NotYetInitializedException();
		long offsetFromBaseAddress = sizeOfClassInBytes * index;
		long objectAddress = baseAddressInMemory + offsetFromBaseAddress;
		
		Object[] helperArray = new Object[1];
		long helperBaseOffset = unsafe.arrayBaseOffset(Object[].class);
		long addressOfFirstHelperElement = UnsafeUtils.getAddressOf(helperArray) + helperBaseOffset;
		unsafe.putLong(addressOfFirstHelperElement, objectAddress);
		
		return (E) helperArray[0];
	}

	/**
	 * Writes an object to the array.
	 * 
	 * As the object provided will be copied to the array, you should use the object returned by the method. 
	 * @param obj The object to write to the array.
	 * @param index The index inside the array. Zero based.
	 * @return The element that has been stored in the array.
	 */
	public E set(E obj, int index) {
		if(index < 0 || index >= length)
			throw new ArrayIndexOutOfBoundsException(String.format("Tried to access element at index %d.", index));
		initialized[index] = true;
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
}
