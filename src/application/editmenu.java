package application;

import javafx.stage.Stage;
import util.AbstractWindow;




public class editmenu extends AbstractWindow {

    @Override
    public void start(Stage stage) {
        try {
            super.setFilename("view/editmenu.fxml");
            super.start(stage);
            stage.setTitle("Manager Menu");
            stage.setResizable(true);
        } catch (Exception e) {
            System.out.println("Couldn't find the fxml file.");
        }
    }
}
