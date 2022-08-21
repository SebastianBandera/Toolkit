package fx.comps.cell.value.factories;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fx.controllers.main.MainTableDataRow;
import fx.controllers.main.ModuleStatus;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import toolkit.core.api.module.ModuleCapabilitiesEnum;
import toolkit.core.modules.ManagedModule;

public class CellValueCustomAction implements Callback<CellDataFeatures<MainTableDataRow, fx.comps.cell.value.factories.CellValueCustomAction.CustomType>, ObservableValue<fx.comps.cell.value.factories.CellValueCustomAction.CustomType>> {

	private static ObservableValue<CustomType> EMPTY = new ReadOnlyObjectWrapper<CustomType>();
	
    public class CustomType extends SplitMenuButton {
    	public CustomType() {
    		super();
    	}
    }

    private final Map<String, ObservableValue<CustomType>> cache;

	public CellValueCustomAction() {
		this.cache    = new HashMap<>();
	}
	
	private final static String TXT_RUN = "Run";
	private final static String TXT_CONFIG = "Configure";
	private final static String TXT_REQUEST_PAUSE = "Request pause";
	private final static String TXT_RESUME_PAUSE = "Resume";
	private final static String TXT_REQUEST_STOP = "Request stop";

	@Override
	public ObservableValue<CustomType> call(CellDataFeatures<MainTableDataRow, CustomType> cellData) {
		MainTableDataRow data = (MainTableDataRow)cellData.getValue();
		if (data == null) {
			return EMPTY;
		}
		
		String id = data.getPropertyId().get();
		
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		
		CellValueCustomActionHelper helper = new CellValueCustomActionHelper(data);
		
    	ObservableValue<CustomType> oLbl = helper.getCell();
    	
    	cache.put(id, oLbl);
    	
		return oLbl;
	}
	
    
	//Subclass
    private class CellValueCustomActionHelper {
    	
    	private final MainTableDataRow data;
    	private final ManagedModule mm;
    	
    	private SimpleObjectProperty<CustomType> property = null;
    	
    	private CustomType btn 		   = null;
    	private MenuItem mRun  		   = null;
    	private MenuItem mConfigure    = null;
		private MenuItem mRequestPause = null;
		private MenuItem mRequestStop  = null;
		private EventHandler<ActionEvent> run    = null;
		private EventHandler<ActionEvent> pause  = null;
		private EventHandler<ActionEvent> resume = null;
		private EventHandler<ActionEvent> config = null;
		private EventHandler<ActionEvent> stop   = null;

		public CellValueCustomActionHelper(MainTableDataRow data) {
    		this.data = data;
    		if (data!=null) {
				this.mm = data.getManagedModule();
			} else {
				this.mm = null;
			}
    		
    		this.property = null;
    	}
    	
    	public SimpleObjectProperty<CellValueCustomAction.CustomType> getCell() {
    		if (mm == null) {
				return null;
			}
    		
    		//Instance elements
    		btn = new CustomType();
    		run    = event -> this.run(data);
    		pause  = event -> this.pause(data);
    		resume = event -> this.resume(data);
    		config = event -> this.config(data);
    		stop   = event -> this.stop(data);
    		
    		//Do bindings
    		
    		
    		//Define events
    		
        	
        	//Customs
    		ObservableList<MenuItem> items = btn.getItems();
        	if (mm.can(ModuleCapabilitiesEnum.RUNNABLE)) {
        		mRun = new MenuItem(TXT_RUN);
        		mRun.setOnAction(run);
    		}
        	if (mm.can(ModuleCapabilitiesEnum.CONFIGURABLE)) {
        		mConfigure = new MenuItem(TXT_CONFIG);
        		mConfigure.setOnAction(config);
    		}
        	if (mm.can(ModuleCapabilitiesEnum.PAUSABLE)) {
            	mRequestPause = new MenuItem(TXT_REQUEST_PAUSE);
            	mRequestPause.setOnAction(pause);
    		}
        	if (mm.can(ModuleCapabilitiesEnum.STOPPABLE)) {
            	mRequestStop  = new MenuItem(TXT_REQUEST_STOP);
            	mRequestStop.setOnAction(stop);
    		}
        	
        	controlButton(items);
        	
        	this.property = new SimpleObjectProperty<CustomType>(btn);
        	
        	return property;
    	}
    	
    	@SuppressWarnings("unused")
		public SimpleObjectProperty<CustomType> getProperty() {
			return this.property;
		}
    	
    	private void controlButton(ObservableList<MenuItem> items) {
    		ModuleStatus status = data.getStatus().get();
        	switch (status) {
    		case IDLE:
    			if (mRun != null) {
    				btn.setText(TXT_RUN);
    				btn.setOnAction(run);
    				btn.setVisible(true);
    				setSubOptions(items, mRun, mConfigure, mRequestPause, mRequestStop, mRun, status);
    			} else if (mConfigure != null) {
    				btn.setText(TXT_CONFIG);
    				btn.setOnAction(config);
    				btn.setVisible(true);
    				setSubOptions(items, mRun, mConfigure, mRequestPause, mRequestStop, mConfigure, status);
    			} else {
    				btn.setVisible(false);
    			}
    			break;
    		case RUNNING:
    			if (mRequestPause != null) {
    				btn.setText(TXT_REQUEST_PAUSE);
    				btn.setOnAction(pause);
    				btn.setVisible(true);
    			} else if (mRequestStop != null) {
    				btn.setText(TXT_REQUEST_STOP);
    				btn.setOnAction(stop);
    				btn.setVisible(true);
    			} else if (mConfigure != null) {
    				btn.setText(TXT_CONFIG);
    				btn.setOnAction(config);
    				btn.setVisible(true);
    			} else {
    				btn.setVisible(false);
    			}
    			break;
    		case PAUSED:
    			btn.setText(TXT_RESUME_PAUSE);
    			btn.setOnAction(resume);
    			btn.setVisible(true);
    			break;
    		case ERROR:
    			btn.setOnAction(null);
    			btn.setVisible(false);
    			break;
    		}
    	}
    	
    	private void setSubOptions(ObservableList<MenuItem> items, MenuItem mRun, MenuItem mConfigure, MenuItem mRequestPause, MenuItem mRequestStop, MenuItem exclude, ModuleStatus status) {
    		if (!items.isEmpty()) {
				return;
			}
    		
    		List<MenuItem> preList = new LinkedList<>();
    		if (mRun !=null && mRun != exclude) {
    			preList.add(mRun);
    			mRun.setDisable(status != ModuleStatus.IDLE);
    		}
    		if (mConfigure !=null && mConfigure != exclude) {
    			preList.add(mConfigure);
    		}
    		if (mRequestPause !=null && mRequestPause != exclude) {
    			preList.add(mRequestPause);
    			mRequestPause.setDisable(status != ModuleStatus.RUNNING);
    		}
    		if (mRequestStop !=null && mRequestStop != exclude) {
    			preList.add(mRequestStop);
    			mRequestStop.setDisable(status != ModuleStatus.RUNNING);
    		}
    		
    		
    		items.addAll(preList);
    	}

    	private void run(MainTableDataRow row) {
    		ManagedModule mm = row.getManagedModule();
        	mm.submitTask(() -> mm.getUserInterface().runAction());

        	row.getStatus().set(ModuleStatus.RUNNING);
        	controlButton(btn.getItems());
        	
        	row.getTimesUsed().set(row.getTimesUsed().get()+1);
        }
        
        private void config(MainTableDataRow row) {
    		ManagedModule mm = row.getManagedModule();
        	mm.submitTask(() -> mm.getConfigurableInterface().config());
        	
        	controlButton(btn.getItems());
        }
        
        private void pause(MainTableDataRow row) {
    		ManagedModule mm = row.getManagedModule();
        	mm.submitTask(() -> {
    			try {
    				mm.getPausableInterface().requestPause();
    			} catch (InterruptedException e) {
    				System.out.println("Error in pause " + mm.getIdentifier());
    				e.printStackTrace();
    			}
    		});

        	row.getStatus().set(ModuleStatus.PAUSED);
        	controlButton(btn.getItems());
        }
        
        private void resume(MainTableDataRow row) {
    		ManagedModule mm = row.getManagedModule();
        	mm.submitTask(() -> mm.getPausableInterface().releasePause());
        	
        	controlButton(btn.getItems());
        }
        
        private void stop(MainTableDataRow row) {
    		ManagedModule mm = row.getManagedModule();
        	mm.submitTask(() -> mm.getStoppableInterface().requestStop(false));

        	row.getStatus().set(ModuleStatus.IDLE);
        	controlButton(btn.getItems());
        }
    }
}

