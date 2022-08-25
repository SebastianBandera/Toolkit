package toolkit.core.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import common.string.StringUtils;
import toolkit.core.info.StatusDeliverer;

public class FileConfigurationController {

	private final static String FILE_NAME_CONFIG = "config.json";
	private final static String FILE_NAME_ENCODING = "UTF-8";
	private final static int   FILE_JSON_IDENT = 4;
	private final Charset CHARSET;
	private final File CONFIG_FILE;
	private final Path CONFIG_FILE_PATH;
	private final StatusDeliverer statusDeliverer;
	
	private ConfigurationData data;
	
	public FileConfigurationController(File root, DirectoriesController dirControl, StatusDeliverer statusDeliverer) {
		this.statusDeliverer = statusDeliverer;
		
		String child = String.join(File.separator, dirControl.getFileMain().getName(), FILE_NAME_CONFIG);
		CONFIG_FILE = new File(root, child);
		CONFIG_FILE_PATH = CONFIG_FILE.toPath();
		
		CHARSET = Charset.forName(FILE_NAME_ENCODING);
		
		if (CONFIG_FILE.exists()) {
			statusDeliverer.deliveryStatus("Config file found in " + CONFIG_FILE.getAbsolutePath());
			try {
				String json = new String(Files.readAllBytes(CONFIG_FILE_PATH), CHARSET);
			
				try {
					data = new ConfigurationData(json);
					statusDeliverer.deliveryStatus("Config read: " + CONFIG_FILE.getAbsolutePath());
				} catch (Exception e) {
					statusDeliverer.deliveryStatus(StringUtils.concat("Config file found in ", CONFIG_FILE.getAbsolutePath(), ". But the format was invalid."));
					
					generateNewConfig();
				}
			} catch (Exception e) {
				statusDeliverer.deliveryStatus(StringUtils.concat("Can't read config file in ", CONFIG_FILE.getAbsolutePath()));
			}
		} else {
			statusDeliverer.deliveryStatus(StringUtils.concat("Config file not found in ", CONFIG_FILE.getAbsolutePath()));

			generateNewConfig();			
		}
	}
	
	private void generateNewConfig() {
		data = new ConfigurationData();
		
		try {
			saveData();

			statusDeliverer.deliveryStatus(StringUtils.concat("New config file generated and saved in " + CONFIG_FILE.getAbsolutePath()));
		} catch (Exception e) {
			statusDeliverer.deliveryStatus(StringUtils.concat("Error while saving the new config file. It will try to set the default options in memory."));
		}		
	}
	
	public ConfigurationData getData() {
		return this.data;
	}
	
	public void saveData() throws IOException {
		List<String> dataToWrite = new LinkedList<>();
		dataToWrite.add(data.getJSON().toString(FILE_JSON_IDENT));
		
		Files.write(CONFIG_FILE_PATH, dataToWrite, CHARSET, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
}
