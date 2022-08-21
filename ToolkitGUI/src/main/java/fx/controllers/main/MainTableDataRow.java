package fx.controllers.main;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import toolkit.core.modules.ManagedModule;

public class MainTableDataRow {

	private SimpleStringProperty id;
	private SimpleStringProperty description;
	private SimpleStringProperty comment;
	private SimpleStringProperty actionButton;
	private SimpleObjectProperty<ModuleStatus> status;
	private SimpleIntegerProperty timesUsed;
	private SimpleBooleanProperty autoRunOnStartup;
	private SimpleStringProperty autoRunText;
	
	private ManagedModule managedModule;

	public MainTableDataRow() {
		this.id      	      = new ReadOnlyStringWrapper("");
		this.description      = new ReadOnlyStringWrapper("");
		this.comment	      = new SimpleStringProperty();
		this.actionButton     = new SimpleStringProperty();
		this.status           = new SimpleObjectProperty<ModuleStatus>();
		this.timesUsed        = new SimpleIntegerProperty();
		this.actionButton     = new SimpleStringProperty();
		this.autoRunOnStartup = new SimpleBooleanProperty();
		this.autoRunText      = new SimpleStringProperty();
		
		this.managedModule    = null;
		
		this.status.set(ModuleStatus.IDLE);
	}

	public void setManagedModule(ManagedModule managedModule) {
		this.managedModule = managedModule;
		this.id 		   = new ReadOnlyStringWrapper(managedModule.getIdentifier());
		this.description   = new ReadOnlyStringWrapper(managedModule.getDescription());
	}

	public SimpleStringProperty getPropertyId() {
		return id;
	}
	
	public SimpleStringProperty getPropertyComment() {
		return comment;
	}
	
	public SimpleStringProperty getPropertyDescription() {
		return description;
	}

	public SimpleStringProperty getActionButton() {
		return actionButton;
	}

	public ManagedModule getManagedModule() {
		return managedModule;
	}

	public SimpleObjectProperty<ModuleStatus> getStatus() {
		return status;
	}

	public SimpleIntegerProperty getTimesUsed() {
		return timesUsed;
	}

	public SimpleBooleanProperty getAutoRunOnStartup() {
		return autoRunOnStartup;
	}

	public SimpleStringProperty getAutoRunText() {
		return autoRunText;
	}
	
	public final static String translate(Boolean newValue) {
		if (newValue == null) {
			return "";
		}
		
		if (newValue) {
			return "Yes";
		} else {
			return "No";					
		}
	}
}
