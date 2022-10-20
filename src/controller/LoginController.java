package controller;

import application.SignUp;
import application.Tableview;
import application.Main;
import application.dashbord;
import database.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.PrivilegeEnum;
import model.User;
import util.ScreenController;
import util.UserManager;

/**
 * LoginController contains method for handling all event receive from the
 * UserInterface.
 *
 *
 *
 */
public class LoginController {
	@FXML
	private BorderPane root;

	@FXML
	private BorderPane rootPanel;

	@FXML
	public TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button login;

	@FXML
	private Button cancel;

	@FXML
	private Label connLabel;

	@FXML
	private Alert alert;

	@FXML
	private Hyperlink signUp;

	private static DBManager dbm = DBManager.getInstance();

	/**
	 * Method for handling cancel button. When event receive Main scene is
	 * shown.
	 * 
	 * @param event
	 */
	public void cancelButtonHandler(MouseEvent event) {
		ScreenController.switchWindow((Stage) cancel.getScene().getWindow(), new Main());
	}



	/**
	 * Method for handling login button. When event receive the implementation
	 * below is done. Only login when the username and password match .
	 * 
	 * @param event
	 */
	public void loginButtonHandler(ActionEvent event) {
		if (username.getText().equals("")) {
			showAlert(AlertType.ERROR, "Username is empty!", "Inputfield Error");
		} else if (password.getText().equals("")) {
			showAlert(AlertType.ERROR, "Password is empty!", "Inputfield Error");
		} else {
			int accessLevel = dbm.login(username.getText(), password.getText());
			if (accessLevel == 2) {
				showAlert(AlertType.NONE, "Welcome manager " + username.getText(), "Login Success");


				UserManager.getInstance().setUser(new User(username.getText(), PrivilegeEnum.ADMIN));
				ScreenController.switchWindow((Stage) login.getScene().getWindow(), new dashbord());
			}
			if (accessLevel == 1) {
				showAlert(AlertType.NONE, "Welcome waiter " + username.getText(), "Login Success");
				UserManager.getInstance().setUser(new User(username.getText(), PrivilegeEnum.USER));
				ScreenController.switchWindow((Stage) login.getScene().getWindow(), new dashbord());
			}
			if (accessLevel == 0) {
				showAlert(AlertType.ERROR, "Wrong password!", "Inputfield Error");
				password.clear();
			}
			if (accessLevel == -1) {
				showAlert(AlertType.ERROR, "User does not exist!", "Inputfield Error");
			}
		}

	}


	/*
	 * Method for shortening alert codes.
	 */
	private void showAlert(AlertType alert, String message, String header) {
		this.alert = new Alert(alert, message, ButtonType.OK);
		this.alert.setHeaderText(header);
		this.alert.show();

	}

	public void signUpButtonHandler(MouseEvent event) {
		ScreenController.switchWindow((Stage) signUp.getScene().getWindow(), new SignUp());
	}

}
