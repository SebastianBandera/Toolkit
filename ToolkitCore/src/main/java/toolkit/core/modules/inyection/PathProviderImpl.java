package toolkit.core.modules.inyection;

import java.io.File;

import common.string.StringUtils;
import toolkit.core.api.inyection.PathProvider;

public class PathProviderImpl implements PathProvider {

	private final File installationFile;
	
	private final File tempDirectoryForInstallation;
	private final File storageDirectoryForInstallation;
	
	
	public PathProviderImpl(File modulesTemp, File modulesStorage, File installationFile) {
		this.installationFile = installationFile;
		
		String installationName = getInstallationName(this.installationFile.getName());
		
		this.tempDirectoryForInstallation    = new File(modulesTemp, installationName);
		this.storageDirectoryForInstallation = new File(modulesStorage, installationName);
	}

	private String getInstallationName(String name) {
		if (StringUtils.isNullOrEmptyOrWhiteSpaces(name)) {
			return "noname";
		}
		
		if (name.contains(".")) {
			int lastIndex = name.lastIndexOf('.');
			String result = name.substring(0, lastIndex);
			return result;
		}
		
		return name;
	}

	@Override
	public File getInstallationFile() {
		return this.installationFile;
	}

	@Override
	public File getTempDirectory() {
		return this.tempDirectoryForInstallation;
	}

	@Override
	public File getStorageDirectory() {
		return this.storageDirectoryForInstallation;
	}
	

}
