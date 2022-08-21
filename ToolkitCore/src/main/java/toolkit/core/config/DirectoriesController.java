package toolkit.core.config;

import java.io.File;

import common.file.DirectoryException;
import common.file.FileUtils;

public class DirectoriesController {

	private final String MODULES = "modules";
	private final String MODULES_INSTALLATION = "installation";
	private final String MODULES_STORAGE = "storage_files";
	private final String MODULES_TEMP = "temp_files";
	private final String LOGS = "logs";
	private final String MAIN = "main";
	private final String FX = "fx";
	
	private File fileModules;
	private File fileModulesInstallation;
	private File fileModulesStorage;
	private File fileModulesTemp;
	private File fileLogs;
	private File fileMain;
	private File fileFx;
	
	private File root;
	
	public void createDirs(File root) throws DirectoryException {
		this.root = root;
		
		this.fileModules			   = new File(root, MODULES);
		this.fileModulesInstallation   = new File(this.fileModules, MODULES_INSTALLATION);
		this.fileModulesStorage        = new File(this.fileModules, MODULES_STORAGE);
		this.fileModulesTemp           = new File(this.fileModules, MODULES_TEMP);
		this.fileLogs 				   = new File(root, LOGS);
		this.fileMain 			   	   = new File(root, MAIN);
		this.fileFx   				   = new File(this.fileMain, FX);
		
		FileUtils.requiredMakeDir(this.fileModules);
		FileUtils.requiredMakeDir(this.fileModulesInstallation);
		FileUtils.requiredMakeDir(this.fileModulesStorage);
		FileUtils.requiredMakeDir(this.fileModulesTemp);
		FileUtils.requiredMakeDir(this.fileLogs);
		FileUtils.requiredMakeDir(this.fileMain);
		FileUtils.requiredMakeDir(this.fileFx);
	}
	

	public File getRoot() {
		return root;
	}
	
	public File getFileModules() {
		return fileModules;
	}

	public File getFileModulesInstallation() {
		return fileModulesInstallation;
	}

	public File getFileModulesStorage() {
		return fileModulesStorage;
	}

	public File getFileModulesTemp() {
		return fileModulesTemp;
	}

	public File getFileLogs() {
		return fileLogs;
	}

	public File getFileMain() {
		return fileMain;
	}

	public File getFileFx() {
		return fileFx;
	}
}
