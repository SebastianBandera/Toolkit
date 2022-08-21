package toolkit.core.modules;

import toolkit.core.api.module.ModuleCapabilities;
import toolkit.core.api.module.ModuleCapabilitiesEnum;
import toolkit.core.api.module.capabilities.ConfigurableModule;
import toolkit.core.api.module.capabilities.PausableModule;
import toolkit.core.api.module.capabilities.RunnableModule;
import toolkit.core.api.module.capabilities.StoppableModule;

public interface ManagedModule extends ModuleCapabilities {

	boolean can(ModuleCapabilitiesEnum capability);

	//PRE: corresponding "can" returns true
	RunnableModule getUserInterface();

	//PRE: corresponding "can" returns true
	PausableModule getPausableInterface();

	//PRE: corresponding "can" returns true
	StoppableModule getStoppableInterface();

	//PRE: corresponding "can" returns true
	ConfigurableModule getConfigurableInterface();

	String getIdentifier();

	String getDescription();

	void submitTask(Runnable task);

	void shutdown();

	boolean isValid();

	void setValid();

	void invalidate(String reason);

	String getClassURL();

	ClassLoader getClassLoader();

}