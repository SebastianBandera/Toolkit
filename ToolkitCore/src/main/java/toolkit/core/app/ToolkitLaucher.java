package toolkit.core.app;

import java.util.Arrays;
import java.util.List;

import toolkit.core.info.StatusDeliverer;

//Configure Toolkit App Based on Input Parameters
public class ToolkitLaucher {

	private final static List<String> ROOT_PATH_PARAMETER = Arrays.asList(new String[] {"rootpath", "rt"});
	
	public ToolkitApp prepareApp(String[] args, StatusDeliverer statusDeliverer) {
		ToolkitInitParameters params = getParams(args);
		ToolkitApp app = null;
		try {
			app = new ToolkitApp(params, statusDeliverer);
		} catch (Exception e) {
			System.out.println("ERROR. Initialization problem.");
			e.printStackTrace();
			
			System.exit(1001);
		}
		
		return app;
	}
	
	public ToolkitApp run(String[] args, StatusDeliverer statusDeliverer) {
		ToolkitApp app = prepareApp(args, statusDeliverer);
		
		try {
			app.run();
			return app;
		} catch (Exception e) {
			System.out.println("ERROR. Run problem.");
			e.printStackTrace();

			System.exit(1002);
		}
		
		return null;
	}

	private ToolkitInitParameters getParams(String[] args) {
		ToolkitInitParameters params = new ToolkitInitParameters();

		if (args == null || args.length == 0) {
			return params;
		}
		
		for (String arg : args) {
			int index = arg.indexOf(":");
			if (index != -1) {
				String[] parts = getParts(arg, index);
				if (ROOT_PATH_PARAMETER.contains(parts[0])) {
					params.setRootPath(parts[1]);
				}
			}
		}
		
		return params;
	}

	private String[] getParts(String arg, int index) {
		String[] results = new String[2];
		
		results[0] = arg.substring(1, index).toLowerCase();
		results[1] = arg.substring(index + 1);
		
		return results;
	}
}
