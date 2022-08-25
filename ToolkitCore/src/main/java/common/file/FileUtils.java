package common.file;

import java.io.File;
import java.util.Objects;

public class FileUtils {

	public final static void requiredMakeDir(File file) throws DirectoryException {
		Objects.requireNonNull(file, "Null file");
		
		if (!file.mkdirs()) {
			if (!file.exists()) {
				throw new DirectoryException("Directory not exists", DirectoryException.KnownCases.DIRECTORY_NO_EXISTS);				
			}
			if (!file.isDirectory()) {
				throw new DirectoryException("Is not a directory", DirectoryException.KnownCases.IS_NOT_A_DIRECTORY);
			}
		}
	}
}
