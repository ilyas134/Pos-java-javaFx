package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import application.*;
import database.DBManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Menu;
import model.Table;
import model.categorie;
import util.ImageFactory;
import util.ScreenController;


/**
 * MGTableController(manager) class contains method for handling event from the
 * UserInterface. Contains method for adding or removing menu with picture.
 * 
 *
 *
 */
public class MGEditMenuController extends OrderViewController {
	@FXML
	Button back;
	@FXML
	Button newFood;

	@FXML
	Button deleteImage;

	@FXML
	Button actualiser;
	@FXML
	Button logout;
	@FXML
	public FlowPane foodpane;
	@FXML
	public FlowPane drinkpane;
	String imgpath = null;

	@FXML
	private TextField search;
	
	private static DBManager dbm = DBManager.getInstance();
	private ImageFactory instance = ImageFactory.getInstance();
	public static List<Button> folderImage = new ArrayList<>();


	private Alert alert;

	/**
	 * Initialize the buttons to panes
	 */
	@FXML
	public void initialize() {
		List <categorie> categ=instance.getButton();
		for (int i =0;i<categ.size();i++ ) {
			categ.get(i).getCatButtonList().forEach(x->foodpane.getChildren().add(x));

		}
		search.textProperty().addListener((observable ,oldValue,newValue)->{
			ListProperty<Button> lm=new SimpleListProperty<>();
			List<Button> lmm=new ArrayList<>();

             instance.getButton().forEach(x->{
				 lmm.addAll(x.getCatButtonList());

			 });
			lm.set(FXCollections.observableArrayList(lmm));
			ObservableList<Node> flowChildren = foodpane.getChildren();
			FilteredList<Button> filterdata=new FilteredList<>(lm, b->true);
			SortedList<Button> sortedData= new SortedList<>(filterdata);
			Bindings.bindContent(flowChildren, sortedData);
			foodpane.getChildren().setAll(sortedData);
			filterdata.setPredicate(table->{
				if(newValue==null||newValue.isEmpty()){
					return true;
				}
				String lowercaseFilter=newValue.toLowerCase();
				if(table.getText().toLowerCase().indexOf(lowercaseFilter)!=-1){
					return true;
				}
				 else
					return false;

			});
		});


	}
	
	/**
	 * Get the list of buttons with the set images.
	 * 
	 * @return list of Buttons
	 */
	public static List<Button> getImage() {
		return folderImage;
	}

	/**
	 * Method for handling new Food button. Insert food images into the flow pane.
	 * 
	 * @param event
	 */
	public void insertFoodHandler(ActionEvent event) {
		ScreenController.switchWindow((Stage) newFood.getScene().getWindow(), new editmenu());
	}



	/**
	 * Method for handling new Drink button. Insert drink images into the flow pane.
	 * 
	 * @param event
	 */


	/**
	 * Method for handling newImage button. Insert an image into the flow pane and
	 * database.
	 * 
	 * @param name
	 *            of table in database.
	 */


	/**
	 * Method for handling deleteImage button. Delete the selected button from the
	 * flow pane.
	 * 
	 * @param event
	 */
	public void deleteImageHandler(ActionEvent event) {
		Button button = instance.getSelectedButton();
		Menu menu = null;
		if (button == null) {
			alert = new Alert(AlertType.ERROR, "Must select atleast one dish!", ButtonType.OK);
			alert.show();
		}
		if (foodpane.getChildren().contains(button)) {
			menu = (Menu) instance.getSelectedButton().getUserData();
			foodpane.getChildren().remove(button);

			dbm.removeImage(menu.getCategorie(), menu);
		}
		final String cat=menu.getCategorie();
		instance.getButton().forEach(x->{
			if(x.getCategorie().equals(cat)){
				x.getCatButtonList().remove(button);
			}
		});


	}


	/**
	 * Handler for back button. When event receive the table view scene is shown.
	 * 
	 * @param event
	 */
	public void backButtonHandler(ActionEvent event) {
		ScreenController.switchWindow((Stage) back.getScene().getWindow(), new dashbord());
	}

	/**
	 * Handler for logout button. When event receive the Start up scene is shown.
	 * 
	 * @param event
	 */
	public void logoutHandler(ActionEvent event) {
		ScreenController.switchWindow((Stage) logout.getScene().getWindow(), new Main());
	}

    public void categorie(ActionEvent actionEvent) {

		String[] choices = { "Add categorie", "Remove categorie" };
		List<String> option = new ArrayList<>(Arrays.asList(choices));
		ChoiceDialog<String> dialog = new ChoiceDialog<>("SELECT", option);
		dialog.setTitle("Add categorie");
		dialog.setHeaderText("Please select an operation");
		dialog.setContentText("operation:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result.get().equals("SELECT")) {
				alert = new Alert(AlertType.ERROR, "Please select something!", ButtonType.OK);
				alert.show();
			} else if (result.get().equals(choices[0])) {
				Dialog<String> dialog2 = new TextInputDialog();
				dialog2.setHeaderText("Enter categorie name:");
				Optional<String> result2 = dialog2.showAndWait();
				if (result2.isPresent()) {
					try {
						String input = result2.get();
					   if (dbm.checkkTable(input)) {
							alert = new Alert(AlertType.ERROR, "Categorie exist already!", ButtonType.OK);
							alert.show();
							return;
						} else {
							dbm.insertTableName(result2.get());
							dbm.creeateTable(result2.get());
						   List<Button> btn= new ArrayList<>();
							categorie cat=new categorie(btn,result2.get());
						   instance.getButton().add(cat);

							alert = new Alert(AlertType.INFORMATION, "Categorie created!", ButtonType.OK);

							alert.show();
						}
					} catch (NumberFormatException ex) {
						alert = new Alert(AlertType.ERROR, "Please input numbers only!", ButtonType.OK);
						alert.show();
						return;
					}
				}
			} else if (result.get().equals(choices[1])) {
				List<String> tables = dbm.getDBTTables();
				ChoiceDialog<String> dialog3 = new ChoiceDialog<>("SELECT", tables);
				dialog.setTitle("Remove categorie");
				dialog.setHeaderText("Please select a categorie");
				dialog.setContentText("Categorie:");
				Optional<String> result3 = dialog3.showAndWait();
				if (result3.isPresent()) {

						dbm.deleteTTable(result3.get());
						dbm.removeTTableinTables(result3.get());
					for( int i = 0; i < instance.getButton().size(); i++ )
					{

							if(instance.getButton().get(i).getCategorie().equals(result3.get())){
								instance.getButton().remove(instance.getButton().get(i));
							}
					}



						alert = new Alert(AlertType.WARNING, "Categorie removed!", ButtonType.OK);
						alert.show();
					}
				}
			}
		}
    }

