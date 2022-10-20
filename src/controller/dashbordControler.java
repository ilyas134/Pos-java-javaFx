package controller;



import application.*;
import database.DBManager;
import database.DBObserver;

import java.awt.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import util.ScreenController;
import util.UserManager;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * TableViewController(manager/employee) class contains method for handling
 * event from the UserInterface. Contains method that can look up orders in each
 * table with full permission to manage the restaurant.
 *
 * @author Piyawat & Vichapol
 *
 */
public class dashbordControler implements Observer {
    @FXML
    private Button logout;

    @FXML
    private Button editMenu;

    @FXML
    private Button summary;

    @FXML
    private Button endday;

    @FXML
    private Button manageUser;

    @FXML
    private Button charts;

    @FXML
    private Button tables;
    @FXML
    private Alert alert;
    @FXML
    private FlowPane buttonPane;

    @FXML
    private Button finance;

    @FXML
    private Button dsalary;

    private UserManager um = UserManager.getInstance();
    private static DBManager dbm = DBManager.getInstance();


    private static Timer timer = new Timer();
    private static DBObserver dbo = DBObserver.getInstance();
    private boolean admin = um.isAdmin();
    private int tmpTotal;
    private int i=0;
    static {
        dbo.setAllRows();
        dbo.setChanges();
        runTask();
    }

    public void initialize() {
        if (!admin) {
            editMenu.setDisable(true);
            editMenu.setVisible(false);
            manageUser.setDisable(true);
            manageUser.setVisible(false);

            charts.setDisable(true);
            charts.setVisible(false);
        }
        Stage stage =new Stage();
        stage.setResizable(false);
        dbo.addObserver(this);
    }

    @FXML
    void financeButtonHandler(MouseEvent event) {
        ScreenController.switchWindow((Stage) editMenu.getScene().getWindow(), new managermoney());
    }

    public void editMenuButtonHandler(MouseEvent event) {
        ScreenController.switchWindow((Stage) editMenu.getScene().getWindow(), new MGEditMenu());
    }
    public void tables (MouseEvent event) {
        ScreenController.switchWindow((Stage) editMenu.getScene().getWindow(), new Tableview());
    }
    /**
     * Handler for Manage user button. When event receive the Manage menu scene
     * is shown.
     *
     * @param event
     */
    public void manageUserButtonHandler(MouseEvent event) {
        ScreenController.switchWindow((Stage) manageUser.getScene().getWindow(), new ManageUser());
    }

    public void charButtonHandler(MouseEvent event) {
        ScreenController.switchWindow((Stage) manageUser.getScene().getWindow(), new chart());

    }

    /**
     * Handler for logout button. When event receive the login scene is shown.
     *
     * @param event
     */
    public void logoutButtonHandler(ActionEvent event) {
        alert = new Alert(AlertType.CONFIRMATION, "Vous êtes sur de quitter?", ButtonType.OK);
        alert.setHeaderText("Confirmation");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ScreenController.switchWindow((Stage) logout.getScene().getWindow(), new Login());
            }
        });
    }

    /**
     * Handler for summary button. When event receive summary dialod is shown.
     *
     * @param event
     */
    public  ArrayList<Integer> search(TextArea area,String txt) throws Exception{
        ArrayList<Integer> lis =new ArrayList();
        String[] words=null;
        String s;
        String m="";
        BufferedReader entree = new BufferedReader(new StringReader(area.getText()));
        while((s=entree.readLine())!=null)   //Reading Content from the file
        {   int i=0;
            m+=s;
            s = s.replaceAll("\\s", "");

            words=s.split("\\*");  //Split the word using space
            for (String word : words)
            {
                word = word.replace("\n", "");
                if (word.equals(txt))   //Search for the given word
                {
                    for( i=0;i<m.length();i++){

                    }
                System.out.println("index"+area.getText().indexOf(txt));
                    System.out.println(i);
               lis.add(i );//If Present increase the count by one
                }
            }
        }for(int i=0;i<lis.size();i++){
        }
        return lis;
    }




    /**
     * Handler for end day button. When event receive all data in summary is
     * cleared and the program is close.
     *
     * @param event
     */
    public void endButtonHandler(MouseEvent event) {
        alert = new Alert(AlertType.WARNING,
                "Are you sure to ENDDAY, all table(s) including added table(s) ordered item(s) will be remove without paying and will not be add to summary, this operation can't be undone?",
                ButtonType.OK);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert = new Alert(AlertType.WARNING, "Vous etes sur ?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response2 -> {
                    if (response2 == ButtonType.YES) {
                        for (int i = 1; i <= 8; i++) {
                            String table = "table" + i;
                            dbm.clearTable(table);
                        }

                        try {
                            dbm.insertTostat(dbm.getDBOrders("summary"));

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
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

    }

}

