import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.data.NotYetInitializedException;
import ch.epfl.data.UnsafeArray;

public class UnsafeArrayTest {

	UnsafeArray<SimpleTestclass> arr;
	SimpleTestclass o1;
	SimpleTestclass o2;
	SimpleTestclass o3;
	SimpleTestclass o4;
	SimpleTestclass o5;
	@Before
	public void setUp() throws Exception {
		arr = new UnsafeArray<SimpleTestclass>(SimpleTestclass.class, 5);
		o1 = new SimpleTestclass(1, 'h', 'p', 6.4d);
		o2 = new SimpleTestclass(77, 'j', 'a', 4.5d);
		o3 = new SimpleTestclass(48, 't', 'q', 123.86d);
		o4 = new SimpleTestclass(8869, 'u', 's', 66d);
		o5 = new SimpleTestclass(2833, 'e', 'r', 3.66d);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSet() {
		SimpleTestclass ret0 = arr.copyAndSet(o1, 0);
		SimpleTestclass ret4 = arr.copyAndSet(o4, 4);
		
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
		SimpleTestclass ret1 = arr.copyAndSet(o1, 2);
		assertEquals(o1, ret1);
		SimpleTestclass ret2 = arr.copyAndSet(o2, 2);
		assertEquals(o2, ret2);
		assertEquals(o2, ret1);
	}
	
	@Test(expected = NotYetInitializedException.class)
	public void testGetUnitialized() throws NotYetInitializedException {
		arr.get(1);
	}
	
	@Test
	public void testSetAndGet() throws NotYetInitializedException {
		SimpleTestclass set3 = arr.copyAndSet(o3, 3);
		SimpleTestclass get3 = arr.get(3);
		assertEquals(set3, o3);
		assertSame(set3, get3);
		
	}
	
	@Test
	public void testGetAndEdit() throws NotYetInitializedException {
		arr.copyAndSet(o3, 3);
		SimpleTestclass ret0 = arr.get(3);  
		ret0.b = 's';
		SimpleTestclass ret1 = arr.get(3);
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
		new UnsafeArray<SimpleTestclass>(SimpleTestclass.class, 0);
	}
	
	@Test
	public void testReleasingMemory() {
		UnsafeArray<SimpleTestclass> arr;
		for(int i = 0; i < 100000; i++) {
			arr = new UnsafeArray<SimpleTestclass>(SimpleTestclass.class, 10000);
			if(i%100 == 0) System.gc(); // Unclear behaviour. Needed for JDK1.7.0.75 x86 on Linux Mint.
		}
	}
}
