package common.structural;

public class Pair<A, B> {

	private A a;
	private B b;
	
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getKey() {
		return a;
	}

	public void setKey(A a) {
		this.a = a;
	}

	public B getValue() {
		return b;
	}

	public void setValue(B b) {
		this.b = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair)) {
			return false;
		}
		return this.getKey().equals(((Pair<A, B>)obj).getKey()) && this.getValue().equals(((Pair<A, B>)obj).getValue());
	}

	@Override
	public String toString() {
		return "key=" + this.getKey().toString() + ",value=" + this.getValue().toString();
	}
}
