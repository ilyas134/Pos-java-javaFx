package application;

import database.DBManager;
import javafx.stage.Stage;
import util.AbstractWindow;
import util.ImageFactory;

import java.util.List;


/**
 * Main class that runs StartUp of the program with three options. Login,
 * SignUP, CustomerMode. Also get the data from database.
 * 
 *
 *
 */
public class Main extends AbstractWindow  {
	private static DBManager dbm = DBManager.getInstance();
	@Override
	public void start(Stage stage) {
		try {
			ImageFactory img = ImageFactory.getInstance();


			List<String> temp = dbm.getDBTTables();
			for (String tablenum : temp) {
				img.loadImage(tablenum);
			}
			super.setFilename("view/login.fxml");
			super.start(stage);
			stage.setTitle("Start");
		} catch (Exception e) {
			System.out.println("Couldn't find the fxml file.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DBManager.getInstance();
		launch(args);
	}
}
