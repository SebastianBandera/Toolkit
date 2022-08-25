package toolkit.core.modules.reflection;

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
import toolkit.core.modules.ManagedModule;
import toolkit.core.modules.ModuleThreadFactory;

public class ManagedReflexiveModule implements ManagedModule {

	private final Object originalModule;
	private final Module module;
	
	private RunnableModule  runnableInterface;
	private PausableModule  pausableInterface;
	private StoppableModule stoppableInterface;
	private ConfigurableModule configurableInterface;
	
	private final ExecutorService executor;
	
	private final ClassLoader classLoader;
	private final String classURL;
	private final Class<?> cls;
	
	private String invalidReason;
	
	public ManagedReflexiveModule(Object object, ClassLoader classLoader, String classURL, Class<?> cls) {
		this.originalModule = Objects.requireNonNull(object);
		this.classLoader    = classLoader;
		this.classURL       = classURL;
		this.cls            = cls;
		this.module         = new ReflexiveModule(this.originalModule, this.cls);
		
		if (!implementsInterfaceCompliantModule(this.cls)) {
			this.runnableInterface     = null;
			this.pausableInterface     = null;
			this.stoppableInterface    = null;
			this.configurableInterface = null;
			
			this.invalidate("Not a module");
		} else {
			if (implementsInterfaceCompliantRunnable(this.cls))  this.runnableInterface     = new RunnableReflexiveModule(object, this.cls);
			else this.runnableInterface     = null;
			
			if (implementsInterfaceCompliantPausable(this.cls))  this.pausableInterface = new PausableReflexiveModule(object, this.cls);
			else this.pausableInterface     = null;
			
			if (implementsInterfaceCompliantStoppable(this.cls)) this.stoppableInterface     = new StoppableReflexiveModule(object, this.cls);
			else this.stoppableInterface    = null;
			
			if (implementsInterfaceCompliantConfigurable(this.cls)) this.configurableInterface = new ConfigurableReflexiveModule(object, this.cls);
			else this.configurableInterface = null;
			
			this.setValid();	
		}		
		
		this.executor = new ThreadPoolExecutor(1, 10, 5L, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ModuleThreadFactory(this.getIdentifier()));
	}
	
	private final boolean implementsInterfaceCompliantModule(Class<?> cls) {
		try {
			cls.getMethod("getIdentifier");
			cls.getMethod("getDescription");
			
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	
	private final boolean implementsInterfaceCompliantRunnable(Class<?> cls) {
		try {
			cls.getMethod("runAction");
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	
	private final boolean implementsInterfaceCompliantConfigurable(Class<?> cls) {
		try {
			cls.getMethod("config");
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	
	private final boolean implementsInterfaceCompliantPausable(Class<?> cls) {
		try {
			cls.getMethod("requestPause");
			cls.getMethod("releasePause");
			cls.getMethod("isPauseRequested");
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	
	private final boolean implementsInterfaceCompliantStoppable(Class<?> cls) {
		try {
			cls.getMethod("requestStop", boolean.class);
			cls.getMethod("isStopRequested");
			return true;
		} catch (Exception e) {
			return false;
		}		
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
}
