package toolkit.core.api.module.capabilities;

import toolkit.core.api.module.Module;

public interface StoppableModule extends Module {

	void requestStop(boolean closingModule);

	boolean isStopRequested();
}
