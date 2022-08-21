package fx.comps.cell.factories;

import java.util.function.Function;

import fx.controllers.main.MainTableDataRow;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class CellFactoryLabelGeneric<T> implements Callback<TableColumn<MainTableDataRow, T>, TableCell<MainTableDataRow, T>> {

	private final Function<T, String> translator;
	
	public CellFactoryLabelGeneric(Function<T, String> translator) {
		this.translator = translator;
	}
	
	@Override
	public TableCell<MainTableDataRow, T> call(TableColumn<MainTableDataRow, T> param) {
		return new TableCell<MainTableDataRow, T>() {
			private final Label   lbl     = new Label();
			private final Tooltip tooltip = new Tooltip();
			
            {
            	lbl.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);
            	lbl.setWrapText(true);
            	
            	setTooltipIfValue();
            }
            
            private final void setTooltipIfValue() {
            	String text = lbl.getText();
				tooltip.setText(text);
				if (text == null || text.isEmpty()) {
					lbl.setTooltip(null);
				} else {
					lbl.setTooltip(tooltip);
				}
            }

            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                	if (item!=null) {
                		lbl.setText(translator.apply(item));						
					} else {
						lbl.setText("");
					}
                	setTooltipIfValue();
                    setGraphic(lbl);
                }
            }
		};
	}

}
