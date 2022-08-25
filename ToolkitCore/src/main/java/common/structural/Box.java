package common.structural;

//Can use Optional from java.util.Optional ? Inmutable is usually preferable
public class Box<T> {

	private T data;
	
	public Box() {
		this.data = null;
	}
	
	public Box(T data) {
		this.data = data;
	}
	
	public void setData(T data) {
		this.data = data;
	}

	public void clear() {
		this.data = null;
	}
	
	public T getData() {
		return this.data;
	}

	@Override
	protected Box<T> clone() {
		return new Box<T>(data);
	}
}
