package toolkit.core.config;

import java.io.File;

import common.file.DirectoryException;
import toolkit.core.info.StatusDeliverer;

public class MainConfiguration {
	
	private final DirectoriesController dirControl;
	private final StatusDeliverer statusDeliverer;
	private final File root;
	
	private FileConfigurationController fileConfiguration;
	
	public MainConfiguration(File root, StatusDeliverer statusDeliverer) throws DirectoryException {
		dirControl = new DirectoriesController();
		dirControl.createDirs(root);
		
		this.statusDeliverer = statusDeliverer;
		this.root = root;
	}
	
	public DirectoriesController getDirectoriesController() {
		return this.dirControl;
	}

	public synchronized FileConfigurationController getFileConfiguration() {
		if (fileConfiguration == null) {
			fileConfiguration = new FileConfigurationController(root, dirControl, statusDeliverer);
		}
		
		return fileConfiguration;
	}
}
