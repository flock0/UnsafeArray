public class HelloWorld {

	public static void main(String[] args) {
		System.out.println(UnsafeUtils.sizeof(Testklasse.class));
		UnsafeArray<Testklasse> usarr = new UnsafeArray<Testklasse>(Testklasse.class, 1); 
		Testklasse obj0 = new Testklasse(55);
		usarr.set(obj0, 0);
		Testklasse retr0 = usarr.get(0);
		System.out.println(retr0.x);
	}
}
