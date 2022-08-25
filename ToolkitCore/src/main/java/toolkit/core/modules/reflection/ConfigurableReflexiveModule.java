package toolkit.core.modules.reflection;

import java.lang.reflect.Method;

import toolkit.core.api.module.capabilities.ConfigurableModule;

public class ConfigurableReflexiveModule extends ReflexiveModule implements ConfigurableModule  {
	
	public ConfigurableReflexiveModule(Object module, Class<?> cls) {
		super(module, cls);
	}

	@Override
	public void config() {
		try {
			Method m = this.cls.getMethod("config");
			m.invoke(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
