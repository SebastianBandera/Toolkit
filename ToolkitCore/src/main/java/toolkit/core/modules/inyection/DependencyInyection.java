package toolkit.core.modules.inyection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import common.structural.Coalesce;
import toolkit.core.api.inyection.Locker;
import toolkit.core.api.inyection.PathProvider;
import toolkit.core.api.inyection.PausableModuleHelper;
import toolkit.core.api.inyection.ToolkitNotifier;
import toolkit.core.signaling.lock.SemaphoreLocker;

public class DependencyInyection {

	//Singleton dependencies
	private final Map<Class<?>, Object> staticPool;
	//Scoped dependencies to module
	private final Map<Class<?>, BiFunction<String, File, Object>> dynamicPoolPerModule;
	
	public DependencyInyection(File modulesTemp, File modulesStorage) {
		this.staticPool           = new HashMap<>();
		this.dynamicPoolPerModule = new HashMap<>();
		
		this.dynamicPoolPerModule.put(PathProvider.class,         (moduleId, file) -> new PathProviderImpl(modulesTemp, modulesStorage, file));
		this.dynamicPoolPerModule.put(ToolkitNotifier.class,      (moduleId, file) -> new ToolkitNotifierImpl(moduleId));
		this.dynamicPoolPerModule.put(Locker.class,               (moduleId, file) -> new SemaphoreLocker());
		this.dynamicPoolPerModule.put(PausableModuleHelper.class, (moduleId, file) -> new PausableModuleHelperImpl(new SemaphoreLocker(), new ToolkitNotifierImpl(moduleId)));
	}

	//String moduleId, File originFile -> to map ??
	public Object getDependencyBy(Class<?> type, String moduleId, File originFile) {
		try {
			return Coalesce.coalesceWithSuppliers(
					() -> this.staticPool.get(type),
					() -> this.dynamicPoolPerModule.get(type).apply(moduleId, originFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
