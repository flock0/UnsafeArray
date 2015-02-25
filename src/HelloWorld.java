import sun.misc.Unsafe;

public class HelloWorld {

	private static Unsafe unsafe = UnsafeUtils.getUnsafe();
	
	public static void main(String[] args) throws Exception {
		 System.out.println(UnsafeUtils.sizeof(Testklasse.class));
		 UnsafeArray<Testklasse> usarr = new
		 UnsafeArray<Testklasse>(Testklasse.class, 2);
		Testklasse obj0 = new Testklasse(55);
//		System.out.println(UnsafeUtils.addressOf(unsafe
//				.allocateInstance(Testklasse.class)));
//		System.out.println(UnsafeUtils.addressOf(obj0));
				 Testklasse retr0 = usarr.set(obj0, 1);
				 retr0.x = 123;
				 System.out.println(usarr.get(1).x);
//		 Testklasse retr0 = usarr.get(0);
//		 System.out.println(retr0.x);
//		Object mine = "Hi there".toCharArray();
//		long address = UnsafeUtils.addressOf(mine);
//		System.out.println("Addess: " + address);
//		System.out.println("Klassengröße: "
//				+ UnsafeUtils.sizeof(Testklasse.class));
//
//		// Verify address works - should see the characters in the array in the
//		// output
//		printBytes(address, 27);

	}
	
	public static void printBytes(long objectAddress, int num) {
		for (long i = 0; i < num; i += 2) {
			int cur = unsafe.getByte(objectAddress + i);
			System.out.print((char) cur);
		}
		System.out.println();
	}
}
