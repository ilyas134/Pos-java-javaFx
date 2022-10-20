package application;

import javafx.stage.Stage;
import util.AbstractWindow;

public class managermoney extends AbstractWindow {
    @Override
    public void start(Stage stage) {
        try {
            super.setFilename("view/manager-money.fxml");
            super.start(stage);
            stage.setTitle("User Manager");
            stage.setResizable(true);
        } catch (Exception e) {
            System.out.println("Couldn't find the fxml file.");
        }
    }
}
