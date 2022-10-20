package application;

import java.util.List;

import controller.OrderViewController;
import javafx.stage.Stage;
import model.Menu;
import util.AbstractWindow;

/**
 * OrderView(manager/employee) class that extends AbstractWindow for capable of
 * running the method switchWindow. This window is for selecting the food,
 * observing customer's order, and check their bills.
 * 
 *
 *
 */
public class OrderView extends AbstractWindow {

	public OrderView(String input, List<Menu> foodnames, List<Menu> drinknames) {
		OrderViewController.setTable(input);
		OrderViewController.setMenu(foodnames, drinknames);
	}
	public OrderView() {

	}



	public OrderView(String input) {
		OrderViewController.setTable(input);
	}

	public void start(Stage stage) {
		try {
			super.setFilename("view/orderview.fxml");
			super.start(stage);
			stage.setTitle("Menu");
			stage.setResizable(true);
		} catch (Exception e) {
			System.out.println("Couldn't find the fxml file.");
			e.printStackTrace();
		}
	}
}
