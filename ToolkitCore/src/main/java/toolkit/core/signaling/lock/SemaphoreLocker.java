package toolkit.core.signaling.lock;
import java.util.concurrent.Semaphore;

import toolkit.core.api.inyection.Locker;

public class SemaphoreLocker implements Locker {

	private final Semaphore semaphore;
	
	public SemaphoreLocker() {
		semaphore = new Semaphore(0);
	}
	
	@Override
	public void ownPause() throws InterruptedException {
		semaphore.acquire();
	}

	@Override
	public void release() {
		semaphore.release();
	}
}
