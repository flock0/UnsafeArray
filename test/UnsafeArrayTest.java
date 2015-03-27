import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.data.NotYetInitializedException;
import ch.epfl.data.UnsafeArray;

public class UnsafeArrayTest {

	UnsafeArray<ShallowTestclass> arr;
	ShallowTestclass o1;
	ShallowTestclass o2;
	ShallowTestclass o3;
	ShallowTestclass o4;
	ShallowTestclass o5;
	@Before
	public void setUp() throws Exception {
		arr = new UnsafeArray<ShallowTestclass>(ShallowTestclass.class, 5);
		o1 = new ShallowTestclass(1, 'h', 'p', 6.4d);
		o2 = new ShallowTestclass(77, 'j', 'a', 4.5d);
		o3 = new ShallowTestclass(48, 't', 'q', 123.86d);
		o4 = new ShallowTestclass(8869, 'u', 's', 66d);
		o5 = new ShallowTestclass(2833, 'e', 'r', 3.66d);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSet() {
		ShallowTestclass ret0 = arr.copyAndSet(o1, 0);
		ShallowTestclass ret4 = arr.copyAndSet(o4, 4);
		
		// The objects should be equal, but 
		// the references don't refer to the same object in memory
		assertEquals(o1, ret0);
		assertNotSame(o1, ret0);
		assertEquals(o4, ret4);
		assertNotSame(o4, ret4);
	}
	
	@Test
	public void testSetIndexOutOfBoundsShouldFail() {
		int exceptionCount = 0;
		try{
			arr.copyAndSet(o2, 5);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.copyAndSet(o2, -1);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.copyAndSet(o2, 12345);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		assertEquals(3, exceptionCount);
	}
	
	@Test
	public void testSetMultipleTimesOnSameIndex() {
		ShallowTestclass ret1 = arr.copyAndSet(o1, 2);
		assertEquals(o1, ret1);
		ShallowTestclass ret2 = arr.copyAndSet(o2, 2);
		assertEquals(o2, ret2);
		assertEquals(o2, ret1);
	}
	
	@Test(expected = NotYetInitializedException.class)
	public void testGetUnitialized() throws NotYetInitializedException {
		arr.get(1);
	}
	
	@Test
	public void testSetAndGet() throws NotYetInitializedException {
		ShallowTestclass set3 = arr.copyAndSet(o3, 3);
		ShallowTestclass get3 = arr.get(3);
		assertEquals(set3, o3);
		assertSame(set3, get3);
		
	}
	
	@Test
	public void testGetAndEdit() throws NotYetInitializedException {
		arr.copyAndSet(o3, 3);
		ShallowTestclass ret0 = arr.get(3);  
		ret0.a = 5;
		ShallowTestclass ret1 = arr.get(3);
		assertEquals(ret1, ret0);
		assertNotEquals(o3, ret1);
	}
	
	@Test
	public void testGetIndexOutOfBoundsShouldFail() throws NotYetInitializedException {
		arr.copyAndSet(o4, 4);
		int exceptionCount = 0;
		try{
			arr.get(5);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.get(-1);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.get(44);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		assertEquals(3, exceptionCount);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayLengthTooSmallShouldFail() {
		new UnsafeArray<ShallowTestclass>(ShallowTestclass.class, 0);
	}
	
	@Test
	public void testReleasingMemory() {
		UnsafeArray<ShallowTestclass> arr;
		for(int i = 0; i < 100000; i++) {
			arr = new UnsafeArray<ShallowTestclass>(ShallowTestclass.class, 10000);
			if(i%100 == 0) System.gc(); // Unclear behaviour. Needed for JDK1.7.0.75 x86 on Linux Mint.
		}
	}
	
	@Test
	public void testComplexClassWithString() {
		DeepTestclass c1 = new DeepTestclass(12, 10d, new ShallowTestclass(66, 'a', 'd', 161.5d));
		UnsafeArray<DeepTestclass> complexArr = new UnsafeArray<DeepTestclass>(DeepTestclass.class, 5 );
		DeepTestclass retr1 = complexArr.copyAndSet(c1, 1);
		assertEquals(c1, retr1);
	}
}
