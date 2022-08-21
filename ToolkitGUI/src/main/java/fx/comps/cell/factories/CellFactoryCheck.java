package fx.comps.cell.factories;

import common.string.StringUtils;
import fx.controllers.main.MainTableDataRow;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellFactoryCheck implements Callback<TableColumn<MainTableDataRow, String>, TableCell<MainTableDataRow, String>> {

	public CellFactoryCheck() {
		
	}
	
	@Override
	public TableCell<MainTableDataRow, String> call(TableColumn<MainTableDataRow, String> param) {
		return new TableCell<MainTableDataRow, String>() {
			private MainTableDataRow data  = null;
			private boolean notInitialized = true;
			
			private HBox 	 hbox = null;
			private CheckBox chk  = null;
			private Label    lbl  = null;
			private Tooltip  tooltip = null;

            private void init() {
            	notInitialized = false;
            	
            	TableRow<MainTableDataRow> tableRow = getTableRow();
            	if (tableRow == null) {
					data = null;
				} else {
					data = (MainTableDataRow)tableRow.getItem();
				}
            	
            	if (data == null) {
            		return;
				}
            	
    			hbox    = new HBox();
    			chk     = new CheckBox();
    			lbl     = new Label();
    			tooltip = new Tooltip();
            	
    			lbl.textProperty().bindBidirectional(data.getAutoRunText());
    			chk.selectedProperty().bindBidirectional(data.getAutoRunOnStartup());
    			//¿Esta realizando mas bindings de los necesarios?
    			
            	chk.selectedProperty().addListener((o, oldValue, newValue) -> {
            		data.getAutoRunOnStartup().set(newValue);
            		
            		String label = MainTableDataRow.translate(newValue);
            		data.getAutoRunText().set(label);
            	});
            	
            	hbox.getChildren().addAll(chk, lbl);
            }
            
            private final void setTooltipIfValue() {
            	if (notInitialized) {
            		return;
            	}
        		
            	String text = lbl.getText();
        		if (StringUtils.isNullOrEmptyOrWhiteSpaces(text)) {
        			lbl.setTooltip(null);
        		} else {
        			tooltip.setText(text);
        			lbl.setTooltip(tooltip);
        		}
            }

			public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                	if (notInitialized) {
						init();
					}
                	setTooltipIfValue();
                    setGraphic(hbox);
                }
            }
		};
	}

}
