package toolkit.core.modules.reflection;

import java.lang.reflect.Method;

import toolkit.core.api.module.capabilities.PausableModule;

public class PausableReflexiveModule extends ReflexiveModule implements PausableModule  {
	
	public PausableReflexiveModule(Object module, Class<?> cls) {
		super(module, cls);
	}

	@Override
	public void requestPause() throws InterruptedException {
		try {
			Method m = this.cls.getMethod("requestPause");
			m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void releasePause() {
		try {
			Method m = this.cls.getMethod("releasePause");
			m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseRequested() {
		try {
			Method m = this.cls.getMethod("isPauseRequested");
			return (boolean)m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
