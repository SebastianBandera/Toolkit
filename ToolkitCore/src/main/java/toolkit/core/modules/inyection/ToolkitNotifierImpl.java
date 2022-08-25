package toolkit.core.modules.inyection;

import toolkit.core.api.inyection.ToolkitNotifier;
import toolkit.core.api.inyection.notification.Notification;

public class ToolkitNotifierImpl implements ToolkitNotifier {

	private final String moduleId;

	public ToolkitNotifierImpl(String moduleId) {
		this.moduleId = moduleId;
	}
	
	//Incoming
	@Override
	public void sendNotification(Notification notification) {
		if (notification == null) {
			return;
		}
		System.out.println("Module '" + this.moduleId + "' send a notification: " + notification.toString());
	}
}
