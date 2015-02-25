import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.java2d.pipe.SpanShapeRenderer.Simple;


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
		SimpleTestclass ret0 = arr.set(o1, 0);
		SimpleTestclass ret4 = arr.set(o4, 4);
		
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
			arr.set(o2, 5);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.set(o2, -1);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		try{
			arr.set(o2, 12345);
		} catch(ArrayIndexOutOfBoundsException ex) {
			exceptionCount++;
		}
		
		assertEquals(3, exceptionCount);
	}
	
	@Test
	public void testSetMultipleTimesOnSameIndex() {
		SimpleTestclass ret1 = arr.set(o1, 2);
		assertEquals(o1, ret1);
		SimpleTestclass ret2 = arr.set(o2, 2);
		assertEquals(o2, ret2);
		assertEquals(o2, ret1);
	}
	
	@Test
	public void testGetUnitialized() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSetAndGet() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetAndEdit() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetIndexOutOfBoundsShouldFail() {
		fail("Not yet implemented");
	}
}
