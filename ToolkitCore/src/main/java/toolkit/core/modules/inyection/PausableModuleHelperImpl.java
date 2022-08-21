package toolkit.core.modules.inyection;

import java.util.Objects;

import toolkit.core.api.inyection.Locker;
import toolkit.core.api.inyection.PausableModuleHelper;
import toolkit.core.api.inyection.ToolkitNotifier;
import toolkit.core.api.inyection.notification.BuiltNotifications;

public class PausableModuleHelperImpl implements PausableModuleHelper {

	private final Locker lock;
	private final ToolkitNotifier notifier;
	private boolean requested;
	
	public PausableModuleHelperImpl(Locker lock, ToolkitNotifier notifier) {
		this.requested = false;
		
		this.notifier  = Objects.requireNonNull(notifier, "Notifier null");
		this.lock 	   = Objects.requireNonNull(lock, "Locker null");
	}
	
	public final void requestPause() throws InterruptedException {
		this.requested = true;
	}
	
	public final void awaitIfRequested() throws InterruptedException {
		if (this.requested) {
			this.notifier.sendNotification(BuiltNotifications.getDefaultWillPauseNotification());
			this.lock.ownPause();
		}
	}
	
	public final void release() {
		this.requested = false;
		this.lock.release();
	}
	
	public boolean isPauseRequested() {
		return this.requested;
	}
}
