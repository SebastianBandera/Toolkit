package run;

import java.io.File;
import java.util.List;

import fx.controllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import toolkit.core.app.ToolkitApp;
import toolkit.core.app.ToolkitLaucher;
import toolkit.core.info.StatusDeliverer;
import util.FormFXContainer;

@SuppressWarnings("restriction")
public class ToolkitGUI extends Application {

	private ToolkitApp app;
	private StatusDeliverer statusDeliverer;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		statusDeliverer = new StatusDeliverer();
		List<String> list = getParameters().getRaw();
		app = new ToolkitLaucher().prepareApp(list.toArray(new String[list.size()]), statusDeliverer);
		
		FXMLLoader loader = new FXMLLoader(new File(app.getMainConfiguration().getDirectoriesController().getFileFx(), "Main.fxml").toURI().toURL());

		FormFXContainer<MainController> fxMainContainer = new FormFXContainer<MainController>(loader.load(), loader.getController());
		
		fxMainContainer.getController().setToolkitApp(app);
		
		Scene scene = new Scene(fxMainContainer.getParent());
		
		primaryStage.setX(250);
		primaryStage.setY(50);
	
		primaryStage.setTitle("Toolkit");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
		
		new Thread(() -> {
			try {
				app.run();
				
				fxMainContainer.getController().populateTable();
			} catch (Exception e) {
				System.out.println("ERROR. Runtime problem.");
				e.printStackTrace();
				
		        Platform.exit();
				System.exit(1002);
			}
		}, "Thread app runner").start();
	}

}
