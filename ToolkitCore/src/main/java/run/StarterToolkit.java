package run;

import toolkit.core.app.ToolkitLaucher;
import toolkit.core.info.StatusDeliverer;

//Objective, delegate to a non-static method
public class StarterToolkit {

	public static void main(String[] args) {
		new ToolkitLaucher().run(args, new StatusDeliverer());
	}
}
