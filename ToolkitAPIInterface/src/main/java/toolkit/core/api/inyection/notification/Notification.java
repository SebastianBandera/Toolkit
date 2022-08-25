package toolkit.core.api.inyection.notification;

import java.util.Objects;

public class Notification {

	private final NotificationType type;
	private final String message;
	
	public Notification(NotificationType type) {
		Objects.requireNonNull(type, "Null type in notification");
		this.type    = type;
		this.message = "";
	}
	
	public Notification(NotificationType type, String message) {
		Objects.requireNonNull(type, "Null type in notification");
		Objects.requireNonNull(message, "Null message in notification");
		this.type    = type;
		this.message = message;
	}

	public NotificationType getType() {
		return type;
	}
	
	public String getMessage() {
		return this.message;
	}
}
