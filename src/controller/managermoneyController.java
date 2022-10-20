package controller;

import application.dashbord;
import database.DBManager;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Order;
import model.Table;
import model.User;
import util.ScreenController;
import util.UserManager;

import java.sql.SQLException;
import java.util.*;

public class managermoneyController {

    @FXML
    private Button back;

    @FXML
    private TextField searchtxt;

    @FXML
    private Button creditt;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button oder;

    @FXML
    private Button check;

    @FXML
    private Button search;
    @FXML
    private TableView<Table> table;
    @FXML
    private Button supprimer;

    @FXML
    private Button suprtout;
    @FXML
    private TableColumn<Table, String> idc;

    @FXML
    private TableColumn<Table, String> nomc;

    @FXML
    private TableColumn<Table, String> prenomc;
    @FXML
    private Button total;

    @FXML
    private TextArea command;
    @FXML
    private ChoiceBox <String>  filter;
    @FXML
    private TableColumn<Table, String> adressec;
    private DBManager dbm = DBManager.getInstance();
    private ListProperty<Table> listProperty = new SimpleListProperty<>();
    private List<Table> listname = dbm.getDBmoney();
    private List<Table> newlist;
    private String m1;
    private String m2;
    private String m3;
    private UserManager um = UserManager.getInstance();
    private boolean admin = um.isAdmin();
    List<Table> name = new ArrayList<>();
    List<Table> name2 = new ArrayList<>();
    FilteredList <Table> filterdata=new FilteredList<>(listProperty,b->true);
    SortedList<Table> sortedData= new SortedList<>(filterdata);
    ObservableList list1= FXCollections.observableArrayList();
    public void initialize() {    loaddate ();
        ObservableList<Table> oblist=FXCollections.observableArrayList();


        listname.forEach(x -> {
         name.add(new Table(x.getTable(),x.getDate(),x.getEtat(),x.getTotal()));



        });


        idc.setCellValueFactory(new PropertyValueFactory<>("table"));
        nomc.setCellValueFactory(new PropertyValueFactory<>("total"));
        prenomc.setCellValueFactory(new PropertyValueFactory<>("date"));
        adressec.setCellValueFactory(new PropertyValueFactory<>("etat"));



        table.itemsProperty().unbind();
        listProperty.set(FXCollections.observableArrayList(name));
        table.itemsProperty().bind(listProperty);
        table.itemsProperty().unbind();
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);


        searchtxt.textProperty().addListener((observable ,oldValue,newValue)->{
            filterdata.setPredicate(table->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowercaseFilter=newValue.toLowerCase();
                if(table.getTable().toLowerCase().indexOf(lowercaseFilter)!=-1){
                    return true;
                }else if(table.getDate().toLowerCase().indexOf(lowercaseFilter)!=-1){
                    return true;
                }
                  else if(table.getEtat().toLowerCase().indexOf(lowercaseFilter)!=-1){
                    return true;
                }else
                    return false;

            });
        });
        filter.getSelectionModel().selectedItemProperty().addListener ((v,oldValue,newValue)-> {
                    if(newValue.equals(m1)){


                        newlist = dbm.getDBmoney(newValue);
                        name.clear();
                        newlist.forEach(x -> {


                            name.add(new Table(x.getTable(),x.getDate(),x.getEtat(),x.getTotal()));



                        });
                        idc.setCellValueFactory(new PropertyValueFactory<>("table"));
                        nomc.setCellValueFactory(new PropertyValueFactory<>("total"));
                        prenomc.setCellValueFactory(new PropertyValueFactory<>("date"));
                        adressec.setCellValueFactory(new PropertyValueFactory<>("etat"));


                        table.itemsProperty().unbind();
                        listProperty.set(FXCollections.observableArrayList(name));
                        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
                        ArrayList<Table> ls=new ArrayList<>(hs);
                        listProperty.setValue(FXCollections.observableList(ls));
                        table.itemsProperty().bind(listProperty);
                        table.itemsProperty().unbind();
                        table.itemsProperty().unbind();
                        sortedData.comparatorProperty().bind(table.comparatorProperty());
                        table.setItems(sortedData);
                        newlist.clear();

                    }
                    else if(newValue.equals(m3)){
                        name.clear();
                        listname.forEach(x -> {

                            name.add(new Table(x.getTable(),x.getDate(),x.getEtat(),x.getTotal()));



                        });
                        idc.setCellValueFactory(new PropertyValueFactory<>("table"));
                        nomc.setCellValueFactory(new PropertyValueFactory<>("total"));
                        prenomc.setCellValueFactory(new PropertyValueFactory<>("date"));
                        adressec.setCellValueFactory(new PropertyValueFactory<>("etat"));

                        table.itemsProperty().unbind();
                        listProperty.set(FXCollections.observableArrayList(name));
                        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
                        ArrayList<Table> ls=new ArrayList<>(hs);
                        listProperty.setValue(FXCollections.observableList(ls));
                        table.itemsProperty().bind(listProperty);
                        table.itemsProperty().unbind();
                        table.itemsProperty().unbind();
                        sortedData.comparatorProperty().bind(table.comparatorProperty());
                        table.setItems(sortedData);
                    }
                    else if(newValue.equals(m2)){

                        newlist = dbm.getDBmoney(newValue);
                        name.clear();
                        newlist.forEach(x -> {


                            name.add(new Table(x.getTable(),x.getDate(),x.getEtat(),x.getTotal()));



                        });
                        idc.setCellValueFactory(new PropertyValueFactory<>("table"));
                        nomc.setCellValueFactory(new PropertyValueFactory<>("total"));
                        prenomc.setCellValueFactory(new PropertyValueFactory<>("date"));
                        adressec.setCellValueFactory(new PropertyValueFactory<>("etat"));


                        table.itemsProperty().unbind();
                        listProperty.set(FXCollections.observableArrayList(name));
                        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
                        ArrayList<Table> ls=new ArrayList<>(hs);
                        listProperty.setValue(FXCollections.observableList(ls));
                        table.itemsProperty().bind(listProperty);
                        table.itemsProperty().unbind();
                        table.itemsProperty().unbind();
                        sortedData.comparatorProperty().bind(table.comparatorProperty());
                        table.setItems(sortedData);
                        newlist.clear();
                    }
            table.itemsProperty().unbind();
                }
        );

    }
    @FXML
    void showcommand(MouseEvent event) throws SQLException {
        command.clear();
        String text = Order.getInstance().ordeerToooText(dbm.getDBbOorders("summary",table.getSelectionModel().getSelectedItem().getTable(),table.getSelectionModel().getSelectedItem().getDate()));
        command.setStyle("-fx-font-size: 1.3em;-fx-font-family: monospace;");
        command.setText(text);
    }

    @FXML
    void backButtonHandler(ActionEvent event) {
        ScreenController.switchWindow((Stage) back.getScene().getWindow(), new dashbord());

    }

    @FXML
    void carteButtonHandler(MouseEvent event) throws SQLException {

        dbm.updatemoney(table.getSelectionModel().getSelectedItem(),"carte");
         listname = dbm.getDBmoney();

        listProperty.forEach(x -> {
             if((x.getTable().equals(table.getSelectionModel().getSelectedItem().getTable())) &&(x.getDate().equals(table.getSelectionModel().getSelectedItem().getDate()))){
                 x.setEtat("carte");

             }
        });

        listProperty.set(table.getSelectionModel().getSelectedIndex(),table.getSelectionModel().getSelectedItem());
        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
        ArrayList<Table> ls=new ArrayList<>(hs);
        listProperty.setValue(FXCollections.observableList(ls));
   }

    @FXML
    void cashButtonHandler(MouseEvent event) throws SQLException {

        dbm.updatemoney(table.getSelectionModel().getSelectedItem(),"cash");
        listname = dbm.getDBmoney();

        listProperty.forEach(x -> {
            if((x.getTable().equals(table.getSelectionModel().getSelectedItem().getTable())) &&(x.getDate().equals(table.getSelectionModel().getSelectedItem().getDate()))){
                x.setEtat("cash");

            }
        });
        listProperty.set(table.getSelectionModel().getSelectedIndex(),table.getSelectionModel().getSelectedItem());
        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
        ArrayList<Table> ls=new ArrayList<>(hs);
        listProperty.setValue(FXCollections.observableList(ls));
    }

    @FXML
    void creditButtonHandler(MouseEvent event) throws SQLException {

        dbm.updatemoney(table.getSelectionModel().getSelectedItem(),"credit");
        listname = dbm.getDBmoney();

        listProperty.forEach(x -> {
            if((x.getTable().equals(table.getSelectionModel().getSelectedItem().getTable())) &&(x.getDate().equals(table.getSelectionModel().getSelectedItem().getDate()))){
                x.setEtat("credit");

            }
        });
        listProperty.set(table.getSelectionModel().getSelectedIndex(),table.getSelectionModel().getSelectedItem());
        LinkedHashSet<Table> hs=new LinkedHashSet<>(listProperty);
        ArrayList<Table> ls=new ArrayList<>(hs);
        listProperty.setValue(FXCollections.observableList(ls));
    }



    @FXML
    void totalButtonHandler(MouseEvent event) {
        ArrayList<Integer>text=new ArrayList<>();
        Dialog<String> dialog2 = new TextInputDialog();
        dialog2.setHeaderText("Enter the date as DD/MM/YYYY:");
        Optional<String> result2 = dialog2.showAndWait();
        if (result2.isPresent()) {
            try {

                 text = dbm.getDBmm(result2.toString().substring(9,19));
                String txt=" cash:"+text.get(0)+" \n Carte bancaire:"+text.get(1)+" \n Crédit:"+text.get(2)+"\n Total:"+(text.get(0)+text.get(1)+text.get(2));
                Stage stage = new Stage();
                HBox box = new HBox();
                box.setPrefHeight(250);

                box.setPadding(new Insets(15));
                box.setAlignment(Pos.TOP_LEFT);
                TextArea area = new TextArea();
                area.setStyle("-fx-font-family:monospace");
                area.setStyle("-fx-font-size: 1.5em");

                area.setPrefWidth(600);
                area.appendText(txt);


                area.setEditable(false);
                box.getChildren().add(area);
                Scene scene = new Scene(box);
                stage.setScene(scene);
                stage.setTitle("Total");
                stage.sizeToScene();
                stage.show();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
    private void loaddate(){
        list1.removeAll(list1);
        m1 ="Ordres par jour";
          m2 ="Ordres par semaine";
        m3 ="Tout les ordres";

        list1.addAll(m1,m2,m3);
        filter.getItems().addAll(list1);
    }


    public void supprimer(MouseEvent mouseEvent) {
        if(admin){

            dbm.removeOrderDB(table.getSelectionModel().getSelectedItem().getDate());
            listProperty.remove(table.getSelectionModel().getSelectedItem());
            sortedData.remove(table.getSelectionModel().getSelectedItem());

            filterdata.remove(table.getSelectionModel().getSelectedItem());
        }
        else{ Dialog<String> dialog = new Dialog<>();


            dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            PasswordField pwd = new PasswordField();
            HBox content = new HBox();
            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Entrer le password du manager :"), pwd);
            dialog.getDialogPane().setContent(content);


            Optional<String> result2 = dialog.showAndWait();
            String rslt=pwd.getText();
            if (result2.isPresent()) {

                if(dbm.login( rslt)==2){
                    dbm.removeOrderDB(table.getSelectionModel().getSelectedItem().getDate());
                    listProperty.remove(table.getSelectionModel().getSelectedItem());
                    sortedData.remove(table.getSelectionModel().getSelectedItem());

                    filterdata.remove(table.getSelectionModel().getSelectedItem());
                }else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "password erroné ", ButtonType.OK);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {

                        }
                    });
                }


            }

        }

    }

    public void suprtout(MouseEvent mouseEvent) {
        if(admin){
            dbm.removeOrderDB();
            listProperty.clear();
            sortedData.clear();

            filterdata.clear();
        }
        else{ Dialog<String> dialog = new Dialog<>();


            dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            PasswordField pwd = new PasswordField();
            HBox content = new HBox();
            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Entrer le password du manager :"), pwd);
            dialog.getDialogPane().setContent(content);


            Optional<String> result2 = dialog.showAndWait();
            String rslt=pwd.getText();
            if (result2.isPresent()) {

                if(dbm.login( rslt)==2){
                    dbm.removeOrderDB();
                    listProperty.clear();
                    sortedData.clear();

                    filterdata.clear();
                }else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "password erroné ", ButtonType.OK);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {

                        }
                    });
                }


            }

        }


    }
}
