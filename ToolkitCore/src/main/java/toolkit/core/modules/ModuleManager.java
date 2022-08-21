package toolkit.core.modules;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import common.string.StringUtils;
import toolkit.core.config.DirectoriesController;
import toolkit.core.config.MainConfiguration;
import toolkit.core.info.StatusDeliverer;
import toolkit.core.modules.inyection.DependencyInyection;

public class ModuleManager {

	private final MainConfiguration mainConfiguration;
	private final DependencyInyection dependencyInyection;
	private final StatusDeliverer statusDeliverer;
	private final List<ManagedModule> modules;
	
	public ModuleManager(MainConfiguration mainConfiguration, StatusDeliverer statusDeliverer) {
		this.mainConfiguration = mainConfiguration;
		this.statusDeliverer   = statusDeliverer;
		
		DirectoriesController dController = mainConfiguration.getDirectoriesController();
		final File modulesTmp     = dController.getFileModulesTemp();
		final File modulesStorage = dController.getFileModulesStorage();
		
		this.dependencyInyection  = new DependencyInyection(modulesTmp, modulesStorage);
		
		statusDeliverer.deliveryStatus("Looking for modules...");
		this.modules = getModulesFromProviders(getModuleProviders());
		
		statusDeliverer.deliveryStatus("Extra module validation...");
		checkModulesIdentifiers();
	}
	

	//Get the modules from the providers. Then transforms them to managed module.
	private List<ManagedModule> getModulesFromProviders(List<ModuleProvider> providers) {
		return providers.stream()
						.map(provider -> provider.getModules())
						.reduce(new LinkedList<ManagedModule>(), this::mergeList);
	}
	
	private List<ModuleProvider> getModuleProviders() {
		List<ModuleProvider> providers = new LinkedList<>();
		
		providers.add(new ModuleScanner(this.mainConfiguration, this.dependencyInyection, this.statusDeliverer));
		
		return providers;
	}
	
	public List<ManagedModule> getModules() {
		return modules;
	}
	
	private void checkModulesIdentifiers() {
		boolean hasEmptyId = false;
		List<String> repeatedIds = new LinkedList<>();
		
		Iterator<ManagedModule> iterTest = this.modules.stream().sorted((o1, o2) -> o1.getIdentifier().compareToIgnoreCase(o2.getIdentifier())).iterator();
		String tempId = "";
		while (iterTest.hasNext()) {
			String id = iterTest.next().getIdentifier();
			
			if (StringUtils.isNullOrEmptyOrWhiteSpaces(id)) {
				hasEmptyId = true;
			} else {
				if (id.equals(tempId)) {
					repeatedIds.add(id);
				}				
			}
			
			tempId = id;
		}

		if (hasEmptyId || !repeatedIds.isEmpty()) {
			for (ManagedModule managedModule : this.modules) {
				String id = managedModule.getIdentifier();
				if (hasEmptyId) {
					if (StringUtils.isNullOrEmptyOrWhiteSpaces(id)) {
						managedModule.invalidate("Missing identifier");
					}
				}
				if (!repeatedIds.isEmpty()) {
					if (repeatedIds.contains(id)) {
						managedModule.invalidate(StringUtils.concat("Repeated identifier: '", id, "'"));
					}
				}
			}
		}
	}

	private final <T> List<T> mergeList(List<T> list1, List<T> list2) {
		list1.addAll(list2);
		return list1;
	}
}
