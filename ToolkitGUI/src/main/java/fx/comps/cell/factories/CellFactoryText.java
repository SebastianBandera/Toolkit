package fx.comps.cell.factories;

import fx.controllers.main.MainTableDataRow;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class CellFactoryText implements Callback<TableColumn<MainTableDataRow, String>, TableCell<MainTableDataRow, String>> {

	private final boolean updateByTextChange;
	
	public CellFactoryText(boolean updateByTextChange) {
		this.updateByTextChange = updateByTextChange;
	}
	
	@Override
	public TableCell<MainTableDataRow, String> call(TableColumn<MainTableDataRow, String> param) {
		return new TableCell<MainTableDataRow, String>() {
			private final TextField txt = new TextField();
			private final Tooltip tooltip = new Tooltip();
			
            {
            	txt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            		if (!newValue) {
                		MainTableDataRow data = (MainTableDataRow)getTableRow().getItem();
                		data.getPropertyComment().set(txt.getText());
					}
            	});
            	if (updateByTextChange) {
            		txt.textProperty().addListener((observable, oldValue, newValue) -> {
            			MainTableDataRow data = (MainTableDataRow)getTableRow().getItem();
            			data.getPropertyComment().set(newValue);
            		});					
				} else {
					txt.textProperty().addListener((observable, oldValue, newValue) -> {
						setTooltipIfValue();
					});
				}
            	
            	setTooltipIfValue();
            }
            
            private final void setTooltipIfValue() {
            	String text = txt.getText();
				tooltip.setText(text);
				if (text == null || text.isEmpty()) {
					txt.setTooltip(null);
				} else {
					txt.setTooltip(tooltip);
				}
            }

            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                	setTooltipIfValue();
                    setGraphic(txt);
                }
            }
		};
	}

}
