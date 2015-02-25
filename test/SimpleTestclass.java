
public class SimpleTestclass {
	public int x;
	public char b;
	public char c;
	public double d;
	
	public SimpleTestclass(int x, char b, char c, double d) {
		this.x = x;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + c;
		long temp;
		temp = Double.doubleToLongBits(d);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + x;
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
		SimpleTestclass other = (SimpleTestclass) obj;
		if (b != other.b)
			return false;
		if (c != other.c)
			return false;
		if (Double.doubleToLongBits(d) != Double.doubleToLongBits(other.d))
			return false;
		if (x != other.x)
			return false;
		return true;
	}
	
	
}
