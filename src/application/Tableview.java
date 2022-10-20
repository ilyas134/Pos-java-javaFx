package application;

import javafx.stage.Stage;
import util.AbstractWindow;

/**
 * TableView(manager/employee) class that extends AbstractWindow for capable of running
 * the method switchWindow. The window is for employee to wait for customer
 * response with full permission to remove customer orders.
 * 
 *
 *
 */
public class Tableview extends AbstractWindow {
	@Override
	public void start(Stage stage) {
		try {
			super.setFilename("view/tableview.fxml");
			super.start(stage);
			stage.setTitle("Tableview");
			stage.setResizable(true);
		} catch (Exception e) {
			System.out.println("Couldn't find the fxml file.");
		}
	}
}
