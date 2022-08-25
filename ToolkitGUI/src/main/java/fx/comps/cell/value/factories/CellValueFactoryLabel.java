package fx.comps.cell.value.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fx.controllers.main.MainTableDataRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class CellValueFactoryLabel implements Callback<CellDataFeatures<MainTableDataRow, fx.comps.cell.value.factories.CellValueFactoryLabel.CustomType>, ObservableValue<fx.comps.cell.value.factories.CellValueFactoryLabel.CustomType>> {

	private static ObservableValue<CustomType> EMPTY = new ReadOnlyObjectWrapper<CustomType>();
	
    public class CustomType extends Label {
    	public CustomType() {
    		super();
    	}
    }

	private final Function<MainTableDataRow, SimpleStringProperty> selector;
    private final Map<String, ObservableValue<CustomType>> cache;

	public CellValueFactoryLabel(Function<MainTableDataRow, SimpleStringProperty> selector) {
		this.selector = selector;
		this.cache    = new HashMap<>();
	}

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
		
		//Instance elements
		CustomType lbl = new CustomType();
		Tooltip  tooltip = new Tooltip();
		
		//Do bindings
		lbl.textProperty().bindBidirectional(this.selector.apply(data));
		
		//Define events
    	
    	//Customs
    	TooltipUtils.setTooltipIfValue(lbl, tooltip);
    	lbl.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);
    	lbl.setWrapText(true);
    	
    	//Generate property
    	ObservableValue<CustomType> oLbl = new SimpleObjectProperty<CustomType>(lbl);
    	
    	cache.put(id, oLbl);
    	
		return oLbl;
	}
	


}