package toolkit.core.api.inyection;

import java.io.File;

public interface PathProvider {

	File getInstallationFile();
	
	File getTempDirectory();
	
	File getStorageDirectory();
}
