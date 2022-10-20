package controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;

import application.MGEditMenu;
import application.Tableview;
import application.Main;
import application.dashbord;
import database.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import util.ImageFactory;
import util.ScreenController;


/**
 * MGTableController(manager) class contains method for handling event from the
 * UserInterface. Contains method for adding or removing menu with picture.
 *
 *
 *
 */
public class editmenuController extends MGEditMenuController {
    @FXML
    private Button back;

    @FXML
    private TextField nom;

    @FXML
    private TextField prixv;

    @FXML
    private TextField prixp;

    @FXML
    private ChoiceBox<String> cat;

    @FXML
    private Button btnInserer;

    @FXML
    private ImageView imageview;

    @FXML
    private Button add;

    private static DBManager dbm = DBManager.getInstance();
    private ImageFactory instance = ImageFactory.getInstance();
    public static List<Button> folderImage = new ArrayList<>();
    private Button selectedButton = null;
    private static Order o = Order.getInstance();
    private Alert alert;
    String imgpath = null;
    /**
     * Initialize the buttons to panes
     */
    @FXML
    public void initialize() {
        loadcat();
    }



    public static List<Button> getImage() {
        return folderImage;
    }


    public void deleteImageHandler(ActionEvent event) {
        Button button = instance.getSelectedButton();
        Menu menu = null;
        if (button == null) {
            alert = new Alert(AlertType.ERROR, "Must select atleast one dish!", ButtonType.OK);
            alert.show();
            dbm.removeImage("foods", menu);
        }

    }
    private void loadcat(){
        List<String> tables = dbm.getDBTTables();
        cat.getItems().addAll(tables);
    }
    @FXML
    void ajouter(ActionEvent event) {


        String nomm = nom.getText();
        String prixvv = prixv.getText();
        String prixpp = prixp.getText();
        String categorie =  cat.getValue();





  dbm.insertTo(categorie, nomm, Integer.parseInt(prixvv), Integer.parseInt(prixpp), imgpath);

        Date date =new Date();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        String timme =formater.format(date) ;
        Menu mn = new Menu( Integer.parseInt(prixvv),nomm,Integer.parseInt(prixpp),timme,categorie);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("article");
            alert.setContentText("article est ajouté avec succès");
        Button button = new Button(mn.getName());
        button.setStyle("-fx-background-color: #2596be; -fx-border-color: white;  -fx-text-fill: white;-fx-content-display: top;"  );
        Image image = dbm.getFoodUrl(categorie,nomm);

        ImageView view = new ImageView(image);
        view.setFitHeight(50);
        view.setFitWidth(80);
        button.setPrefSize(120, 70);
        button.setWrapText(true);
        button.setGraphic(view);
        button.setUserData(mn);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            Menu m=(Menu) button.getUserData();
            @Override
            public void handle(MouseEvent event) {
                OrderViewController ov =new OrderViewController();
                o.addOrder(m);
                instance.setSelectedButton(button);
            }
        });
           instance.getButton().forEach(x->{
               if(x.getCategorie().equals(mn.getCategorie())){
                   x.getCatButtonList().add(button);
               }
           });
            alert.showAndWait();
            nom.setText("");
            prixp.setText("");
            prixv.setText("");
            imageview.setImage(null);
    }

    @FXML
    public File choisirImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choisir une image");
        fileChooser.getExtensionFilters().addAll( );
        File selectedFile = fileChooser.showOpenDialog(stage);

        String path = selectedFile.getAbsolutePath();
        imgpath = path;
        Image OriginalPhoto = new Image(selectedFile.toURI().toString());

        imageview.setImage(OriginalPhoto);


        return selectedFile;
    }


    /**
     * Handler for back button. When event receive the table view scene is shown.
     *
     * @param event
     */
    public void backButtonHandler(ActionEvent event) {
        ScreenController.switchWindow((Stage) back.getScene().getWindow(), new MGEditMenu());
    }
}



