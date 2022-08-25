package toolkit.core.info;

public class StatusMessage {

	public enum KnownState {TEXT, MODULE_LOADING}
	
	private final String message;
	private final KnownState state;
	private final double percent;
	
	public StatusMessage(String message) {
		this.message = message;
		this.state   = KnownState.TEXT;
		this.percent = 1.0;
	}
	
	public StatusMessage(String message, KnownState status) {
		this.message = message;
		this.state   = status;
		this.percent = 1.0;
	}
	
	public StatusMessage(String message, KnownState status, double percent) {
		this.message = message;
		this.state   = status;
		this.percent = percent;
	}
	
	public String getMessage() {
		return this.message;
	}

	public KnownState getState() {
		return state;
	}

	public double getProgressPercent() {
		return percent;
	}
}
