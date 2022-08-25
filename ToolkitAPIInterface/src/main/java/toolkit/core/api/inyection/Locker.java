package toolkit.core.api.inyection;

public interface Locker {

	//For internal use of the thread
	void ownPause() throws InterruptedException;
	
	//For public use
	void release();
}
