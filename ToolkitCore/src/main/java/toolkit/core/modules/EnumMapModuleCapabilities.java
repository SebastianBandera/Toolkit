package toolkit.core.modules;

import java.util.EnumMap;

import toolkit.core.api.module.ModuleCapabilities;
import toolkit.core.api.module.ModuleCapabilitiesEnum;

public class EnumMapModuleCapabilities implements ModuleCapabilities {
	
	private EnumMap<ModuleCapabilitiesEnum, Boolean> capabilities;

	public EnumMapModuleCapabilities() {
		this.capabilities = new EnumMap<>(ModuleCapabilitiesEnum.class);
	}
	
	public void set(ModuleCapabilitiesEnum capability, Boolean value) {
		this.capabilities.put(capability, value);
	}
	
	@Override
	public boolean can(ModuleCapabilitiesEnum capability) {
		return this.capabilities.get(capability) == Boolean.TRUE;
	}
}
