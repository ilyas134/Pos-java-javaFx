package application;

import javafx.stage.Stage;
import util.AbstractWindow;

public class dashbord extends AbstractWindow {
    @Override
    public void start(Stage stage) {
        try {
            super.setFilename("view/dashbord.fxml");
            super.start(stage);
            stage.setTitle("dashbord");

            stage.setResizable(true);
        } catch (Exception e) {
            System.out.println("Couldn't find the fxml file.");
        }
    }
}
