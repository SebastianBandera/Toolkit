package toolkit.core.modules;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import common.string.StringUtils;
import common.structural.Box;
import toolkit.core.api.inyection.annotations.Injected;
import toolkit.core.api.module.Module;
import toolkit.core.config.MainConfiguration;
import toolkit.core.info.StatusDeliverer;
import toolkit.core.info.StatusMessage;
import toolkit.core.modules.inyection.DependencyInyection;

public class ModuleScanner implements ModuleProvider {

	private final static String CLASS_EXT     = ".class";
	private final static int    CLASS_EXT_LEN = CLASS_EXT.length();

	private final MainConfiguration   mainConfiguration;
	private final DependencyInyection dependencyInyection;
	private final StatusDeliverer statusDeliverer;
	
	public ModuleScanner(MainConfiguration mainConfiguration, DependencyInyection dependencyInyection, StatusDeliverer statusDeliverer) {
		this.mainConfiguration   = mainConfiguration;
		this.dependencyInyection = dependencyInyection;
		this.statusDeliverer     = statusDeliverer;
	}

	@Override
	public List<ManagedModule> getModules() {
		List<ManagedModule> modules = new LinkedList<>();
		
		Path installation = mainConfiguration.getDirectoriesController().getFileModulesInstallation().toPath();
		
		try {
			List<File> paths = Files.list(installation).map(Path::toFile).collect(Collectors.toList());
			long fileCount = paths.size();
			long currentCount = 0L;
			
			String msgAnalisys = StringUtils.grammaticalInflectionByNumber("Module scanning. Analyzing entries...", '%', fileCount == 1);
			statusDeliverer.deliveryStatus(new StatusMessage(msgAnalisys, StatusMessage.KnownState.MODULE_LOADING, 0.0));
			
			Iterator<File> iter = paths.iterator();
			while (iter.hasNext()) {
				currentCount++;
				File file = iter.next();
				String textMsg = StringUtils.concat("Analysis entry ", String.valueOf(currentCount), "/", String.valueOf(fileCount));
				statusDeliverer.deliveryStatus(new StatusMessage(textMsg, StatusMessage.KnownState.MODULE_LOADING, currentCount/fileCount));
				if (file.isFile()) {
					ManagedModule managedModule = loadModule(file);
					
					if (managedModule!=null) {
						modules.add(managedModule);
					}
				}
			}

			String msgEndAnalisys = StringUtils.grammaticalInflectionByNumber(modules.size() + " module%s found in provider ModuleScanner", '%', modules.size() == 1);
			statusDeliverer.deliveryStatus(new StatusMessage(msgEndAnalisys));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return modules;
	}

	//PRE: asserts file.isFile()
	private ManagedModule loadModule(File file) {
		final Box<ManagedModule> managedModule = new Box<>();
		
		URL[] urls;
		try {
			urls = new URL[1];
			urls[0] = file.toURI().toURL();
		} catch (MalformedURLException e1) {
			return null;
		}
		
		JarFile jar = null;
		
		try {
			//ClassLoader loader = new URLClassLoader(urls, this.getClass().getClassLoader().getParent());
			ClassLoader loader = new URLClassLoader(urls, this.getClass().getClassLoader());
			
			jar = new JarFile(file);
			
			String cacheExe = mainConfiguration.getFileConfiguration().getData().getCacheExecutable(file.getName());
			
			if (StringUtils.isNullOrEmptyOrWhiteSpaces(cacheExe)) {
				Iterator<JarEntry> iter = jar.stream()
											 .filter(entry -> !entry.isDirectory())
									         .filter(entry -> entry.getName()!=null && entry.getName().endsWith(".class"))
									         .iterator();
							
				while (iter.hasNext()) {
					JarEntry entry = iter.next();
					tryLoadClass(entry.getName(), loader, managedModule, file);
				}
			} else {
				tryLoadClass(cacheExe, loader, managedModule, file);
			}
		} catch (Exception e) {
			managedModule.clear();
		} finally {
			if(jar!=null) {
				try {
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return managedModule.getData();
	}
	
	private final void tryLoadClass(String jarEntry, ClassLoader loader, Box<ManagedModule> managedModule, File file) throws Exception {
		String classURLtemp = jarEntry.replace("/",".");
		String classURL = classURLtemp.substring(0, classURLtemp.length() - CLASS_EXT_LEN);
		Class<?> cls = loader.loadClass(classURL);
		if(cls!=null && !cls.isInterface() && implementsModule(cls)) {
			if (managedModule.getData() != null) {
				throw new Exception("Module found two times");
			} else {
				managedModule.setData(new ManagedModuleImpl(createInstance(cls, file), loader, classURL, file));
			}
		}
	}

	private Module createInstance(Class<?> cls, File file) throws Exception {
		Module obj = (Module)cls.newInstance();
		
		for (Field field : cls.getDeclaredFields()) {
			if (field.isAnnotationPresent(Injected.class)) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					field.setAccessible(true);
				}
				field.set(obj, dependencyInyection.getDependencyBy(field.getType(), obj.getIdentifier(), file));
			}
		}
		
		return obj;
	}

	private boolean implementsModule(Class<?> cls) {
		Class<?>[] inter = cls.getInterfaces();
		
		if (inter==null || inter.length == 0) {
			return false;
		}
		
		int i = 0;
		int len = inter.length;
		
		while (i < len) {
			if (inter[i] == Module.class || implementsModule(inter[i])) {
				return true;
			}
			
			i++;
		}
		
		return false;
	}

}
