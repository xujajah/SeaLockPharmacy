package utils;

import javafx.scene.control.TableCell;

public class SerialCell<T> extends TableCell<T, Number> {

	@Override
	protected void updateItem(Number object, boolean empty) {
		if(!empty)
			setText(String.valueOf(getIndex()+1));
		else
			setText("");
	}
}
