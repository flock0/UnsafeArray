package ch.epfl.data.unsafearray;

public class DeepTestclass {
    public int a;
    public double b;
    public ShallowTestclass c;

    public DeepTestclass(int a, double b, ShallowTestclass c) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + a;
        long temp;
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeepTestclass other = (DeepTestclass) obj;
        if (a != other.a)
            return false;
        if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
            return false;
        if (!c.equals(other.c))
            return false;
        return true;
    }

}
