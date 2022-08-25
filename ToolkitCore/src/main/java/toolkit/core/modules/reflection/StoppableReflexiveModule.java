package toolkit.core.modules.reflection;

import java.lang.reflect.Method;

import toolkit.core.api.module.capabilities.StoppableModule;

public class StoppableReflexiveModule extends ReflexiveModule implements StoppableModule  {
	
	public StoppableReflexiveModule(Object module, Class<?> cls) {
		super(module, cls);
	}

	@Override
	public void requestStop(boolean closingModule) {
		try {
			Method m = this.cls.getMethod("requestStop");
			m.invoke(module, closingModule);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isStopRequested() {
		try {
			Method m = this.cls.getMethod("isStopRequested");
			return (boolean)m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
