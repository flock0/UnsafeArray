package ch.epfl.data.unsafearray;

import java.util.Arrays;
import sun.misc.Unsafe;

/**
 * An array implementation that saves objects directly in the memory area of the
 * array.
 * 
 * In difference to the default way arrays are implemented in Java, we don't use
 * an array of references, but store the objects directly in the array fields.
 * When storing an object in the array, the object is copied to the array. Thus,
 * one should not use the initial object, but the object returned by the
 * copyAndSet-method.
 * 
 * Right now, this implementation is only compatible with classes that do not
 * include any fields of a Reference type inside the class itself.
 * 
 * @param <E>
 *            The type of objects stored in the array.
 */
public class UnsafeArray<E> {

    private final static Unsafe unsafe = UnsafeUtils.getUnsafe();
    private final long baseAddressInMemory;
    private final int sizeOfClassInBytes;
    public final int length;
    private final Object[] references;
    private final long arrayBaseOffset;
    private final long scale;
    boolean[] initialized;

    /**
     * Creates a new array which objects are stored directly in the array.
     * 
     * @param type
     *            The class of objects stored in the array (as defined in the
     *            type parameter
     * @param length
     *            The maximum number of elements. Must be > 0
     */
    public UnsafeArray(Class<E> type, int length) {
        if (length < 1)
            throw new IllegalArgumentException("Arraylength is <= 0.");
        this.length = length;
        sizeOfClassInBytes = UnsafeUtils.sizeof(type);
        baseAddressInMemory = unsafe.allocateMemory(sizeOfClassInBytes * length);
        arrayBaseOffset = unsafe.arrayBaseOffset(Object[].class);
        scale = unsafe.arrayIndexScale(Object[].class);
        references = new Object[length];
        initialized = new boolean[length];
        Arrays.fill(initialized, false);
    }

    /**
     * Gets the element at the specified index.
     * 
     * @param index
     *            The index inside the array. Zero based.
     * @return The element at the specified index.
     * @throws NotYetInitializedException
     *             When no object has been set at the field with the provided
     *             index
     */
    public E get(int index) throws NotYetInitializedException {
        if (index < 0 || index >= length)
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "Tried to access element at index %d but length of array is %d.",
                            index, length));
        if (initialized[index] == false)
            throw new NotYetInitializedException();
        return (E) references[index];
    }

    /**
     * Writes an object to the array.
     * 
     * As the object provided will be copied to the array, you should use the
     * object returned by the method.
     * 
     * @param obj
     *            The object to write to the array.
     * @param index
     *            The index inside the array. Zero based.
     * @return The element that has been stored in the array.
     */
    public E copyAndSet(E obj, int index) {
        if (index < 0 || index >= length)
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "Tried to access element at index %d but length of array is %d.",
                            index, length));
        initialized[index] = true;
        long offsetFromBaseAddress = sizeOfClassInBytes * index;
        long sourceAddress = UnsafeUtils.getAddressOf(obj);
        long destAddress = baseAddressInMemory + offsetFromBaseAddress;
        unsafe.copyMemory(sourceAddress, destAddress, sizeOfClassInBytes);

        setObjectInArray(index, destAddress);
        return (E) references[index];
    }

    /**
     * Gets an object from the allocated memory area.
     * 
     * @param index
     *            The index where the object should be saved
     * @param objectAddress
     *            The objects address in memory
     */
    private void setObjectInArray(int index, long objectAddress) {
        long offsetInReferencesArray = arrayBaseOffset + index * scale;
        unsafe.putLong(references, offsetInReferencesArray, objectAddress);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            unsafe.freeMemory(baseAddressInMemory);
        } finally {
            super.finalize();
        }
    }
}
