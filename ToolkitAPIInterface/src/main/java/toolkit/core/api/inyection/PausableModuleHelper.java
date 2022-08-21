package toolkit.core.api.inyection;

public interface PausableModuleHelper {

	void requestPause() throws InterruptedException;
	
	void awaitIfRequested() throws InterruptedException;
	
	void release();
	
	boolean isPauseRequested();
}