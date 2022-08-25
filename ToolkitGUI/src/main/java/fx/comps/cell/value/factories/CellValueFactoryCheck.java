package fx.comps.cell.value.factories;

import java.util.HashMap;
import java.util.Map;

import fx.controllers.main.MainTableDataRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellValueFactoryCheck implements Callback<CellDataFeatures<MainTableDataRow, fx.comps.cell.value.factories.CellValueFactoryCheck.CustomType>, ObservableValue<fx.comps.cell.value.factories.CellValueFactoryCheck.CustomType>> {

	private static ObservableValue<CustomType> EMPTY = new ReadOnlyObjectWrapper<CustomType>();
	
    public class CustomType extends HBox {
    	public CustomType() {
    		super();
    	}
    }
    
    private final Map<String, ObservableValue<CustomType>> cache;

    public CellValueFactoryCheck() {
    	this.cache = new HashMap<>();
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
		CustomType hbox = new CustomType();
		CheckBox chk  = new CheckBox();
		Label    lbl  = new Label();
		Tooltip  tooltip = new Tooltip();
		
		//Do bindings
		lbl.textProperty().bindBidirectional(data.getAutoRunText());
		chk.selectedProperty().bindBidirectional(data.getAutoRunOnStartup());
		
		//Define events
    	chk.selectedProperty().addListener((o, oldValue, newValue) -> {
    		data.getAutoRunOnStartup().set(newValue);
    		
    		String label = MainTableDataRow.translate(newValue);
    		data.getAutoRunText().set(label);
    		
    		TooltipUtils.setTooltipIfValue(lbl, tooltip);
    	});
    	
    	//Append elements
    	hbox.getChildren().addAll(chk, lbl);
    	
    	//Custom action
    	TooltipUtils.setTooltipIfValue(lbl, tooltip);
    	
    	//Generate property
    	ObservableValue<CustomType> oLbl = new SimpleObjectProperty<CustomType>(hbox);
    	
    	cache.put(id, oLbl);
    	
		return oLbl;
	}
	


}
