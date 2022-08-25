package toolkit.core.app;

import java.io.File;
import java.util.Objects;

import common.file.DirectoryException;
import toolkit.core.config.MainConfiguration;
import toolkit.core.info.StatusDeliverer;
import toolkit.core.modules.ModuleManager;

public class ToolkitApp {

	private final MainConfiguration mainConfiguration;
	private final StatusDeliverer statusDeliverer;
	private ModuleManager moduleManager;
	
	public ToolkitApp(ToolkitInitParameters params, StatusDeliverer statusDeliverer) throws DirectoryException {
		Objects.requireNonNull(params, "Null params");
		
		this.statusDeliverer   = statusDeliverer;
		this.mainConfiguration = new MainConfiguration(new File(params.getRootPath().orElse(System.getProperty("user.dir"))), this.statusDeliverer);
	}

	
	public void run() {
		this.moduleManager = new ModuleManager(this.mainConfiguration, this.statusDeliverer);
	}

	public MainConfiguration getMainConfiguration() {
		return mainConfiguration;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public StatusDeliverer getStatusDeliverer() {
		return statusDeliverer;
	}
}
