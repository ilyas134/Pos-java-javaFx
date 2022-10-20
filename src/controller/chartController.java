package controller;

import application.Tableview;
import application.dashbord;
import database.DBManager;
import database.DBObserver;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.Menu;
import util.ScreenController;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class chartController {
    ObservableList list1= FXCollections.observableArrayList();
    @FXML
    private Button backk;
    @FXML
    private Button Show;
    @FXML
    private ChoiceBox <String> month;
    @FXML
    private ChoiceBox <String>  year;
    @FXML
    private BarChart<?,?> linechart;
    @FXML
    private LineChart<?,?> liinechart;
    private static DBManager dbm = DBManager.getInstance();
    private static DBObserver dbo = DBObserver.getInstance();
    static {

        dbo.setChanges();

    }
    @FXML
    public void initialize() {
        loadmonth();
        loadyear();
        month.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
            String m = newValue;
            String y = year.getValue();
            Map<String, Integer> temp2 = new LinkedHashMap<>();
            try {
                temp2= dbm.getDBsstat(m,y);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            linechart.getData().clear();
            liinechart.getData().clear();
            XYChart.Series  series =new  XYChart.Series();
            XYChart.Series  series1 =new  XYChart.Series();
            for (Map.Entry<String, Integer> order : temp2.entrySet()) {
                linechart.getData().clear();
                linechart.setAnimated(false);
                liinechart.getData().clear();
                liinechart.setAnimated(false);
                series.getData().add(new XYChart.Data(order.getKey().substring(0,2),order.getValue()));
                series1.getData().add(new XYChart.Data(order.getKey().substring(0,2),order.getValue()));



            }
            liinechart.getData().addAll(series1);
            linechart.getData().addAll(series);
        });
        year.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
            String m = month.getValue();
            String y = newValue;
            Map<String, Integer> temp2 = new LinkedHashMap<>();
            try {
                temp2= dbm.getDBsstat(m,y);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            linechart.getData().clear();
            liinechart.getData().clear();
            XYChart.Series  series =new  XYChart.Series();
            XYChart.Series  series1 =new  XYChart.Series();
            for (Map.Entry<String, Integer> order : temp2.entrySet()) {
                linechart.getData().clear();
                linechart.setAnimated(false);
                liinechart.getData().clear();
                liinechart.setAnimated(false);

                series.getData().add(new XYChart.Data(order.getKey().substring(0,2),order.getValue()));

                series1.getData().add(new XYChart.Data(order.getKey().substring(0,2),order.getValue()));


            }
            liinechart.getData().addAll(series1);
            linechart.getData().addAll(series);
        });
    }

    @FXML
    public void backk (ActionEvent event) {
        ScreenController.switchWindow((Stage) backk.getScene().getWindow(), new dashbord());

    }
    @FXML
    public void Show (ActionEvent event) throws SQLException {
        String m = month.getValue();
        String y = year.getValue();
        Map<String, Integer> temp2 = new LinkedHashMap<>();
        temp2= dbm.getDBsstat(m,y);
        linechart.getData().clear();
        XYChart.Series  series =new  XYChart.Series();
        for (Map.Entry<String, Integer> order : temp2.entrySet()) {
            linechart.getData().clear();
            linechart.setAnimated(false);

        series.getData().add(new XYChart.Data(order.getKey().substring(0,2),order.getValue()));




    }
        linechart.getData().addAll(series);
}

private void loadmonth(){
        list1.removeAll(list1);
        String m1 ="01";
    String m2 ="02";
    String m3 ="03";
    String m4 ="04";
    String m5 ="05";
    String m6 ="06";
    String m7 ="07";
    String m8 ="08";
    String m9 ="09";
    String m10 ="10";
    String m11 ="11";
    String m12 ="12";
    list1.addAll(m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12);
    month.getItems().addAll(list1);
}
    private void loadyear(){
        list1.removeAll(list1);
        String y1 ="2021";
        String y2 ="2022";
        String y3 ="2023";
        String y4 ="2024";
        String y5 ="2025";
        String y6 ="2026";
        String y7 ="2027";
        String y8 ="2028";
        String y9 ="2029";
        String y10 ="2030";
        String y11 ="2031";
        String y12 ="2032";
        list1.addAll(y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12);
        year.getItems().addAll(list1);
    }


}
