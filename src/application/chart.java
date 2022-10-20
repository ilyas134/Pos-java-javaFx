package application;

import javafx.stage.Stage;
import util.AbstractWindow;

public class chart extends AbstractWindow {
    @Override
    public void start(Stage stage) {
        try {
            super.setFilename("view/stats.fxml");
            super.start(stage);
            stage.setTitle("statistics");
            stage.setResizable(true);
        } catch (Exception e) {
            System.out.println("Couldn't find the fxml file.");
        }
    }
}
