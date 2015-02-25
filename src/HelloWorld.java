import sun.misc.Unsafe;

public class HelloWorld {

	private static Unsafe unsafe = UnsafeUtils.getUnsafe();
	
	public static void main(String[] args) throws Exception {
		 System.out.println(UnsafeUtils.sizeof(SimpleTestclass.class));
		 UnsafeArray<SimpleTestclass> usarr = new
		 UnsafeArray<SimpleTestclass>(SimpleTestclass.class, 2);
		SimpleTestclass obj0 = new SimpleTestclass(55);
				 SimpleTestclass retr0 = usarr.set(obj0, 1);
				 retr0.x = 123;
				 System.out.println(usarr.get(1).x);
				 retr0.x = 5554;
				 System.out.println(usarr.get(1).x);

	}
}
