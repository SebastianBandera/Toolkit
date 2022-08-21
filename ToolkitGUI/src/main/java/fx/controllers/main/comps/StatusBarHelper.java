package fx.controllers.main.comps;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class StatusBarHelper {

    private final Label lblGeneralStatus;
    private final ProgressBar pBar;
    private final ObservableList<String> dataList;
    
    private String currentStatus;
    
    private int limit;
    
    private Object dataListLock = new Object();
    
    private String lastId;
   
	public StatusBarHelper(Label lblGeneralStatus, ProgressBar pBar, int limit) {
		this.lblGeneralStatus = lblGeneralStatus;
		this.pBar             = pBar;
		this.dataList 		  = FXCollections.observableArrayList();
		this.currentStatus    = "";
		
		if (limit > 0) {
			this.limit = limit;			
		} else {
			this.limit = 1;
		}
		
		this.lastId = "";
	}
	
	public String getCurrentStatus() {
		return this.currentStatus;
	}
	
	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		if (lastId == null) {
			this.lastId = "";
		} else {
			this.lastId = lastId;			
		}
	}
	
	public void setStatus(String statusText, String idContextStatus) {
		synchronized (dataListLock) {
			if (this.dataList.isEmpty()) {
				this.dataList.add(statusText);
				this.limit--;
			} else {
				if (this.lastId.equals(idContextStatus)) {
					this.dataList.set(this.dataList.size()-1, statusText);					
				} else {
					addStatus(statusText);
				}
				this.lastId = idContextStatus;
			}
		}
		setStatusLabel(statusText);
	}
	
	public void addStatus(String statusText) {
		synchronized (dataListLock) {
			this.dataList.add(statusText);
			if (this.limit>0) {
				this.limit--;
			} else {
				this.dataList.remove(0);
			}
			this.lastId = "";
		}
		setStatusLabel(statusText);
	}
	
	public void addStatusListener(ListChangeListener<String> listListener) {
		this.dataList.addListener(listListener);
	}
	
	public List<String> getDataList() {
		return new LinkedList<>(this.dataList);
	}
	
	public void setProgress(double progress) {
		Platform.runLater(() -> this.pBar.setProgress(progress));
	}
	
	public void hideProgressBar() {
		Platform.runLater(() -> {
			this.pBar.setVisible(false);
			this.pBar.setManaged(false);
		});
	}
	
	public void showProgressBar() {
		Platform.runLater(() -> {
			this.pBar.setVisible(true);
			this.pBar.setManaged(true);
		});
	}
	
	private void setStatusLabel(String statusText) {
		Platform.runLater(() -> {
			lblGeneralStatus.setText(statusText);
		});
	}
	
}
