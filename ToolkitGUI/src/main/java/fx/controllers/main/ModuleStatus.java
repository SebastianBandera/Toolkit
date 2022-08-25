package fx.controllers.main;

public enum ModuleStatus {

	IDLE("Idle"),
	RUNNING("Running"),
	PAUSED("Paused"),
	ERROR("Error");
	
	private final String desc;
	
	ModuleStatus(String description) {
		this.desc = description;
	}

	public String getDescription() {
		return this.desc;
	}
	
	@Override
	public String toString() {
		return this.desc;
	}
}
