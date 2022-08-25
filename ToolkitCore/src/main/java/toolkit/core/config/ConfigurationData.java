package toolkit.core.config;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationData {

	private JSONObject jsonObj;
	private JSONArray jsonArrayModules;
	private JSONArray jsonArrayFiles;
	
	private static final Object LOCK = new Object();
	
	public ConfigurationData() {
		//this.moduleInfo = new HashMap<>();
		
		this.jsonArrayModules = new JSONArray();
		this.jsonArrayFiles   = new JSONArray();
		this.jsonObj = new JSONObject();
		this.jsonObj.put(Labels.MODULES, this.jsonArrayModules);
		this.jsonObj.put(Labels.FILES, this.jsonArrayFiles);
	}
	
	public ConfigurationData(String json) throws JSONException {
		this.jsonObj = new JSONObject(json);
		this.jsonArrayModules = (JSONArray)jsonObj.get(Labels.MODULES);
		this.jsonArrayFiles = (JSONArray)jsonObj.get(Labels.FILES);
	}
	
	public JSONObject getJSON() {
		synchronized (LOCK) {
			return this.jsonObj;			
		}
	}
	
	public boolean getBackgroundRunning(String moduleId) {
		synchronized (LOCK) {	
			try {
				Iterator<Object> it = this.jsonArrayModules.iterator();
				while (it.hasNext()) {
					JSONObject moduleInfo = (JSONObject) it.next();
					
					String id = moduleInfo.getString(Labels.MODULE_ID);
					if (id != null && id.equals(moduleId)) {
						String backgroundRunning = moduleInfo.getString(Labels.MODULE_AUTORUN);
						return backgroundRunning != null && backgroundRunning.equals("true") ? true : false;
					}
				}
				
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public String getCacheExecutable(String fileName) {
		synchronized (LOCK) {
			try {
				Iterator<Object> it = this.jsonArrayFiles.iterator();
				while (it.hasNext()) {
					JSONObject moduleInfo = (JSONObject) it.next();
					
					String id = moduleInfo.getString(Labels.FILE_NAME);
					if (id != null && id.equals(fileName)) {
						String cacheExecutable = moduleInfo.getString(Labels.FILES_CACHE_EXECUTABLE);
						return cacheExecutable;
					}
				}
				
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	}
	
	private final static class Labels {
		public static final String MODULES = "modules";
		public static final String FILES = "files";
		public static final String FILE_NAME = "fileName";
		public static final String MODULE_ID = "id";
		public static final String MODULE_AUTORUN = "moduleAutorun";
		public static final String FILES_CACHE_EXECUTABLE = "fileCacheExecutable";
		
	}
}
