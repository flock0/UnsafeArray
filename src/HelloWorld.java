import ch.epfl.data.UnsafeArray;
import ch.epfl.data.UnsafeUtils;
import sun.misc.Unsafe;

public class HelloWorld {

    private static Unsafe unsafe = UnsafeUtils.getUnsafe();
    
    public static void main(String[] args) throws Exception {
         System.out.println(UnsafeUtils.sizeof(ShallowTestclass.class));
         UnsafeArray<ShallowTestclass> usarr = new
         UnsafeArray<ShallowTestclass>(ShallowTestclass.class, 2);
         ShallowTestclass obj0 = new ShallowTestclass(55, 'c', 'c', 11);
         ShallowTestclass retr0 = usarr.copyAndSet(obj0, 1);
         retr0.a = 123;
         System.out.println(usarr.get(1).a);
         retr0.a = 5554;
         ShallowTestclass abc = (ShallowTestclass) null;
         System.out.println(usarr.get(1).a);
//      Double d = new Double(5);
//      Unsafe u = UnsafeUtils.getUnsafe();
//      System.out.println(u.addressSize());
//      
//      System.out.println("Address of double " + UnsafeUtils.getAddressOf(d));
//      long reallocMem = u.reallocateMemory(UnsafeUtils.getAddressOf(d) + 48, 16);
//      System.out.println("Memory der realloct wurde und sich mit double überschneidet " + reallocMem);
        
        
    }
}
