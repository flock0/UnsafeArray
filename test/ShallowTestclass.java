
public class ShallowTestclass {
	public int a;
	public double b;
	public ShallowTestclass(int a, char c, char d, double b) {
		super();
		this.a = a;
		this.b = b;
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
		ShallowTestclass other = (ShallowTestclass) obj;
		if (a != other.a)
			return false;
		if (Double.doubleToLongBits(b) != Double
				.doubleToLongBits(other.b))
			return false;
		return true;
	}
	
	
	
}
