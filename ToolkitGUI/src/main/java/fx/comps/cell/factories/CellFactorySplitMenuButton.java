package fx.comps.cell.factories;

import common.structural.Box;
import fx.controllers.main.MainTableDataRow;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import toolkit.core.api.module.ModuleCapabilitiesEnum;
import toolkit.core.modules.ManagedModule;

public class CellFactorySplitMenuButton implements Callback<TableColumn<MainTableDataRow, String>, TableCell<MainTableDataRow, String>> {
	
	public CellFactorySplitMenuButton() {
		
	}
	
	@Override
	public TableCell<MainTableDataRow, String> call(TableColumn<MainTableDataRow, String> param) {
		return new TableCell<MainTableDataRow, String>() {
			private final SplitMenuButton split = new SplitMenuButton();
			private boolean configured = false;
			
//          split.setText("Run          ");
//          split.getItems().addAll(new MenuItem("Configure"), new MenuItem("Request pause"), new MenuItem("Request stop"));

            private void configure() {
            	if (configured) {
					return;
				}
            	
            	MainTableDataRow data = (MainTableDataRow)getTableRow().getItem();
            	
            	if (data == null) {
					return;
				}
            	
            	ManagedModule mm = data.getManagedModule();
            	
            	if (mm.can(ModuleCapabilitiesEnum.RUNNABLE)) {
					split.setText("Run          ");
					split.setOnAction(this::run);
            	} else if(mm.can(ModuleCapabilitiesEnum.CONFIGURABLE)) {
					split.setText("Configure");
					split.setOnAction(this::config);
				} else {
					split.setVisible(false);
					split.setManaged(false);
				}
            	
            	ObservableList<MenuItem> items = split.getItems();
            	if (mm.can(ModuleCapabilitiesEnum.CONFIGURABLE)) {
            		MenuItem mConfigure = new MenuItem("Configure");
            		mConfigure.setOnAction(this::config);
            		items.add(mConfigure);
				}
            	if (mm.can(ModuleCapabilitiesEnum.PAUSABLE)) {
                	MenuItem mRequestPause = new MenuItem("Request pause");
                	mRequestPause.setOnAction(this::pause);
            		items.add(mRequestPause);
				}
            	if (mm.can(ModuleCapabilitiesEnum.STOPPABLE)) {
                	MenuItem mRequestStop  = new MenuItem("Request stop");
                	mRequestStop.setOnAction(this::stop);
            		items.add(mRequestStop);
				}
            	
            	configured = true;
            }
            
            private void run(ActionEvent event) {
            	//TODO: AntiPattern Train Wreck :/
            	ManagedModule mm = ((MainTableDataRow)getTableRow().getItem()).getManagedModule();
            	mm.submitTask(() -> mm.getUserInterface().runAction());
            }
            
            private void config(ActionEvent event) {
            	//TODO: AntiPattern Train Wreck :/
            	ManagedModule mm = ((MainTableDataRow)getTableRow().getItem()).getManagedModule();
            	mm.submitTask(() -> mm.getConfigurableInterface().config());
            }
            
            private void pause(ActionEvent event) {
            	//TODO: AntiPattern Train Wreck :/
            	ManagedModule mm = ((MainTableDataRow)getTableRow().getItem()).getManagedModule();
            	mm.submitTask(() -> {
					try {
						mm.getPausableInterface().requestPause();
					} catch (InterruptedException e) {
						System.out.println("Error in pause " + mm.getIdentifier());
						e.printStackTrace();
					}
				});
            }
            
            private void stop(ActionEvent event) {
            	//TODO: AntiPattern Train Wreck :/
            	ManagedModule mm = ((MainTableDataRow)getTableRow().getItem()).getManagedModule();
            	mm.submitTask(() -> mm.getStoppableInterface().requestStop(false));
            }

            public void updateItem(String item, boolean empty) {
            	configure();
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(split);
                }
            }
		};
	}

}
