package toolkit.core.modules.reflection;

import java.lang.reflect.Method;
import java.util.Objects;

import toolkit.core.api.module.Module;

public class ReflexiveModule implements Module {

	protected final Object module;
	protected final Class<?> cls;
	
	public ReflexiveModule(Object module, Class<?> cls) {
		this.module = Objects.requireNonNull(module);
		this.cls    = Objects.requireNonNull(cls);
	}
	
	@Override
	public String getIdentifier() {
		return getSimpleStringValue("getIdentifier");
	}

	@Override
	public String getDescription() {
		return getSimpleStringValue("getDescription");
	}
	
	private final String getSimpleStringValue(String methodName) {
		try {
			Method m = this.cls.getMethod(methodName);
			Object result = m.invoke(module);
			return (String)result;
		} catch (Exception e) {
			return "";
		}	
	}

}
