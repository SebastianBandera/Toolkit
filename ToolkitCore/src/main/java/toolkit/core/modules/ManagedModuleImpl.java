package toolkit.core.modules;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import common.string.StringUtils;
import toolkit.core.api.module.Module;
import toolkit.core.api.module.ModuleCapabilitiesEnum;
import toolkit.core.api.module.capabilities.ConfigurableModule;
import toolkit.core.api.module.capabilities.PausableModule;
import toolkit.core.api.module.capabilities.RunnableModule;
import toolkit.core.api.module.capabilities.StoppableModule;

public class ManagedModuleImpl implements ManagedModule {

	private final Module module;
	
	private final RunnableModule  runnableInterface;
	private final PausableModule  pausableInterface;
	private final StoppableModule stoppableInterface;
	private final ConfigurableModule configurableInterface;
	
	private final ExecutorService executor;
	
	private final ClassLoader classLoader;
	private final String classURL;
	private final File originFile;
	
	private String invalidReason;
	
	public ManagedModuleImpl(Module module, ClassLoader classLoader, String classURL, File originFile) {
		this.module = Objects.requireNonNull(module);
		this.classLoader = classLoader;
		this.classURL    = classURL;
		this.originFile  = originFile;
		
		if (module instanceof RunnableModule) this.runnableInterface   = (RunnableModule)module;
		else this.runnableInterface = null;
		
		if (module instanceof PausableModule)  this.pausableInterface  = (PausableModule)module;
		else this.pausableInterface     = null;
		
		if (module instanceof StoppableModule) this.stoppableInterface = (StoppableModule)module;
		else this.stoppableInterface    = null;
		
		if (module instanceof ConfigurableModule) this.configurableInterface = (ConfigurableModule)module;
		else this.configurableInterface = null;
		
		this.setValid();
		
		this.executor = new ThreadPoolExecutor(1, 10, 5L, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ModuleThreadFactory(this.getIdentifier()));
	}

	@Override
	public boolean can(ModuleCapabilitiesEnum capability) {
		switch (capability) {
		case RUNNABLE:
			return this.runnableInterface != null;
		case PAUSABLE:
			return this.pausableInterface != null;
		case STOPPABLE:
			return this.stoppableInterface != null;
		case CONFIGURABLE:
			return this.configurableInterface != null;
		default:
			return false;
		}
	}
	
	//PRE: corresponding "can" returns true
	@Override
	public RunnableModule getUserInterface() {
		return this.runnableInterface;
	}
	
	//PRE: corresponding "can" returns true
	@Override
	public PausableModule getPausableInterface() {
		return this.pausableInterface;
	}
	
	//PRE: corresponding "can" returns true
	@Override
	public StoppableModule getStoppableInterface() {
		return this.stoppableInterface;
	}
	
	//PRE: corresponding "can" returns true
	@Override
	public ConfigurableModule getConfigurableInterface() {
		return this.configurableInterface;
	}

	@Override
	public String getIdentifier() {
		return this.module.getIdentifier();
	}

	@Override
	public String getDescription() {
		return this.module.getDescription();
	}
	
	@Override
	public void submitTask(Runnable task) {
		this.executor.submit(task);
	}
	
	@Override
	public void shutdown() {
		this.executor.shutdown();
	}

	@Override
	public boolean isValid() {
		return StringUtils.isNullOrEmptyOrWhiteSpaces(this.invalidReason);
	}

	@Override
	public void setValid() {
		this.invalidReason = null;
	}
	
	@Override
	public void invalidate(String reason) {
		this.invalidReason = reason;
	}

	@Override
	public String getClassURL() {
		return classURL;
	}

	@Override
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public File getOriginFile() {
		return originFile;
	}
}
