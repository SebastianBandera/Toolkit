package toolkit.core.modules.reflection;

import java.lang.reflect.Method;

import toolkit.core.api.module.capabilities.RunnableModule;

public class RunnableReflexiveModule extends ReflexiveModule implements RunnableModule  {
	
	public RunnableReflexiveModule(Object module, Class<?> cls) {
		super(module, cls);
	}
	
	@Override
	public void runAction() {
		try {
			Method m = this.cls.getMethod("runAction");
			m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
