package fx.comps.cell.value.factories;

import common.string.StringUtils;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tooltip;

public final class TooltipUtils {
	
    public final static void setTooltipIfValue(Labeled labeled, Tooltip tooltip) {
    	String text = labeled.getText();
		if (StringUtils.isNullOrEmptyOrWhiteSpaces(text)) {
			labeled.setTooltip(null);
		} else {
			tooltip.setText(text);
			labeled.setTooltip(tooltip);
		}
    }
}
