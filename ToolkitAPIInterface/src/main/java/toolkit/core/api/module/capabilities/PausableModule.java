package toolkit.core.api.module.capabilities;

import toolkit.core.api.module.Module;

public interface PausableModule extends Module {
	
	//The object that inherits this could pause itself if it reads the signal
	void requestPause() throws InterruptedException;

	void releasePause();

	boolean isPauseRequested();
}
