package controller;

import application.*;
import database.DBManager;
import database.DBObserver;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import util.ScreenController;
import util.UserManager;

/**
 * TableViewController(manager/employee) class contains method for handling
 * event from the UserInterface. Contains method that can look up orders in each
 * table with full permission to manage the restaurant.
 *
 * @author Piyawat & Vichapol
 *
 */
public class TableViewController implements Observer {
	@FXML
	private Button editMenu;
	@FXML
	private Button manageUser;
	@FXML
	private Button logout;
	@FXML
	private Button button01;
	@FXML
	private Button button02;
	@FXML
	private Button button03;
	@FXML
	private Button button04;
	@FXML
	private Button button05;
	@FXML
	private Button button06;
	@FXML
	private Button button07;
	@FXML
	private Button button08;
	@FXML
	private Button summary;
	@FXML
	private Button manageTable;
	@FXML
	private Button endday;
	@FXML
	private Button backk;
	@FXML
	private FlowPane buttonPane;
	@FXML
	private Alert alert;

	@FXML
	private Button charts;

	private UserManager um = UserManager.getInstance();
	private static DBManager dbm = DBManager.getInstance();
	private static List<Menu> foodname = dbm.getFoodname("pizza");
	private static List<Menu> drinkname = dbm.getFoodname("boissons");
	private static Timer timer = new Timer();
	private static DBObserver dbo = DBObserver.getInstance();
	private boolean admin = um.isAdmin();
	private int tmpTotal;

	static {
		dbo.setAllRows();
		dbo.setChanges();
		runTask();
	}

	public void initialize() {
		if (!admin) {

		}
		createButton();
		dbo.addObserver(this);


	}
	@FXML
	public void backk (ActionEvent event) {
		ScreenController.switchWindow((Stage) backk.getScene().getWindow(), new dashbord());

	}

	public void button01Handler(ActionEvent event) {
		tableButtonHandler(button01);
	}

	public void button02Handler(ActionEvent event) {
		tableButtonHandler(button02);
	}

	public void button03Handler(ActionEvent event) {
		tableButtonHandler(button03);
	}

	public void button04Handler(ActionEvent event) {
		tableButtonHandler(button04);
	}

	public void button05Handler(ActionEvent event) {
		tableButtonHandler(button05);
	}

	public void button06Handler(ActionEvent event) {
		tableButtonHandler(button06);
	}

	public void button07Handler(ActionEvent event) {
		tableButtonHandler(button07);
	}

	public void button08Handler(ActionEvent event) {
		tableButtonHandler(button08);
	}

	/**
	 * Handler for every table button. When event receive the MGOrder scene is
	 * shown.
	 *
	 * @param button
	 */
	public void tableButtonHandler(Button button) {
		button.setStyle("@tableview.css");
		ScreenController.switchWindow((Stage) button.getScene().getWindow(),
				new OrderView(button.getText(), foodname, drinkname));

	}





	/**
	 * Handler for Manage Table button. When event receive managing table dialog
	 * is shown.
	 *
	 * @param event
	 */
	public void manageTableButtonHandler(MouseEvent event) {
		String[] choices = { "Add table", "Remove table" };
		List<String> option = new ArrayList<>(Arrays.asList(choices));
		ChoiceDialog<String> dialog = new ChoiceDialog<>("SELECT", option);
		dialog.setTitle("Manage Table");
		dialog.setHeaderText("Please select an operation");
		dialog.setContentText("operation:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result.get().equals("SELECT")) {
				alert = new Alert(AlertType.ERROR, "Please select something!", ButtonType.OK);
				alert.show();
			} else if (result.get().equals(choices[0])) {
				Dialog<String> dialog2 = new TextInputDialog();
				dialog2.setHeaderText("Enter table number:");
				Optional<String> result2 = dialog2.showAndWait();
				if (result2.isPresent()) {
					try {
						int input = Integer.parseInt(result2.get());
						if (input < 8 || input >= 100) {
							alert = new Alert(AlertType.ERROR, "Please input numbers between 8 and 99 only!",
									ButtonType.OK);
							alert.show();
							return;
						}
					

						else if (dbm.checkTable(input + "")) {
							alert = new Alert(AlertType.ERROR, "Table exist already!", ButtonType.OK);
							alert.show();
							return;
						} else {
							dbm.insertTableNumber(result2.get());
							dbm.createTable(result2.get());
							createButton();
							alert = new Alert(AlertType.INFORMATION, "Table created!", ButtonType.OK);
							alert.show();
						}
					} catch (NumberFormatException ex) {
						alert = new Alert(AlertType.ERROR, "Please input numbers only!", ButtonType.OK);
						alert.show();
						return;
					}
				}
			} else if (result.get().equals(choices[1])) {
				List<String> tables = dbm.getDBTables();
				ChoiceDialog<String> dialog3 = new ChoiceDialog<>("SELECT", tables);
				dialog.setTitle("Remove Table");
				dialog.setHeaderText("Please select a table");
				dialog.setContentText("table:");
				Optional<String> result3 = dialog3.showAndWait();
				if (result3.isPresent()) {
					if (dbm.checkTableData(result3.get())) {
						dbm.deleteTable(result3.get());
						dbm.removeTableinTables(result3.get());
						createButton();
						alert = new Alert(AlertType.WARNING, "Table removed!", ButtonType.OK);
						alert.show();
					} else {
						alert = new Alert(AlertType.ERROR, "Table still contain orders!", ButtonType.OK);
						alert.show();
					}
				}
			}
		}
	}


	/*
	 * Private method for creating buttons with List<String> from database.
	 */
	private void createButton() {
		buttonPane.getChildren().clear();
		List<String> temp = dbm.getDBTables();
		for (String tablenum : temp) {
			Button button = new Button(tablenum);
			button.setStyle("-fx-background-color: #2596be; -fx-background-radius: 5px;-fx-border-color: white;-fx-border-radius: 5px;-fx-content-display: top;-fx-font-size: 18px;-fx-font-weight: bold;-fx-text-fill: white;"  );
			String filePath = new File("").getAbsolutePath();









 
			button.setWrapText(true);
			button.setPrefSize(150, 150);
			button.setTextAlignment(TextAlignment.CENTER);
			button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					tableButtonHandler(button);
				}
			});
			buttonPane.getChildren().add(button);
		}
	}

	/**
	 * Single implementation to keep track of database table changes.
	 */
	private static void runTask() {
		Runnable runTask = new Runnable() {
			public void run() {
				dbo.findChanges();
				dbo.setChangesBack();
			}
		};
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(runTask);
			}
		};
		timer.schedule(task, 0, 5000);
	}

	@Override
	public void update(Observable o, Object arg) {
		List<Integer> tmp = dbo.getChanges();
		for (int i = 0; i < tmp.size(); i++) {
			if (tmp.get(i) == 1) {
				button01.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 2) {
				button02.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 3) {
				button03.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 4) {
				button04.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 5) {
				button05.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 6) {
				button06.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 7) {
				button07.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			} else if (tmp.get(i) == 8) {
				button08.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;");
			}
		}
	}

}
