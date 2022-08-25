package toolkit.core.api.inyection.notification;

public class BuiltNotifications {

	private static Notification pauseNotification;
	private static Notification stopNotification;
	
	static {
		pauseNotification = new Notification(NotificationType.WILL_PAUSE);
		stopNotification  = new Notification(NotificationType.WILL_STOP);
	}
	
	public static Notification getDefaultWillPauseNotification() {
		return pauseNotification;
	}
	
	public static Notification getDefaultWillStopNotification() {
		return stopNotification;
	}
}
