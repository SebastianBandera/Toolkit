package util;

import javafx.scene.Parent;

public class FormFXContainer<T> {

	private Parent parent;
	private T controller;
	
	public FormFXContainer(Parent parent, T controller) {
		this.parent = parent;
		this.controller = controller;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent pane) {
		this.parent = pane;
	}

	public T getController() {
		return controller;
	}

	public void setController(T controller) {
		this.controller = controller;
	}
}
