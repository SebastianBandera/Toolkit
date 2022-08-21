package toolkit.core.info;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import toolkit.core.info.obs.Observable;
import toolkit.core.info.obs.Observer;

public class StatusDeliverer {

	private final Observable<StatusMessage> obs;
	private final Executor exe;
	
	public StatusDeliverer() {
		this.obs = new Observable<>();
		this.exe = Executors.newSingleThreadExecutor(new StatusDelivererThreadFactory());
	}
	
	public void addObserver(Observer<StatusMessage> o) {
		this.obs.addObserver(o);
	}
	
	public void deliveryStatus(String message) {
		this.exe.execute(() -> {
			this.obs.setChanged();
			this.obs.notifyObservers(new StatusMessage(message));
		});
	}
	
	public void deliveryStatus(StatusMessage msg) {
		this.exe.execute(() -> {
			this.obs.setChanged();
			this.obs.notifyObservers(msg);
		});
	}
}
