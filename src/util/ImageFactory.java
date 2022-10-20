package util;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import controller.OrderViewController;
import database.DBManager;

import javafx.event.EventHandler;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import model.Menu;
import model.Order;

import model.categorie;




/**
 * A class for create button with image graphics which, is pulled from database
 * 
 *
 *
 */
public class ImageFactory {
	protected static ImageFactory factory;
	private static DBManager dbm = DBManager.getInstance();
	private static Order o = Order.getInstance();


	public  List<Button> catButtonList;
	public  List<Button> cattButtonList;
	public   List <categorie> buttonList;
	private Button selectedButton = null;

	/**
	 * Constructor for ImageFactory using lazy instantiation.
	 */
	public ImageFactory() {

		catButtonList = new ArrayList<Button>();
		cattButtonList = new ArrayList<Button>();
		buttonList = new ArrayList<categorie>();

	}

	/**
	 * Get an instance of ImageFactory.
	 * 
	 * @return object of a subclass
	 */
	public static ImageFactory getInstance() {
		if (factory == null)
			factory = new ImageFactory();
		return factory;
	}

	/**
	 * Get the selected button waiting to be removed.
	 * 
	 * @return selected button
	 */
	public Button getSelectedButton() {
		return selectedButton;
	}

	public void setSelectedButton(Button button) {
		this.selectedButton = button;
	}

	/**
	 *
	 * Get all the food's buttons
	 * 
	 * @return buttons of food list
	 */

	public  List <categorie> getButton() {
		return buttonList;
	}

	/**
	 * Get all the drink's buttons
	 * 
	 * @return buttons of drink list
	 */



	/**
	 * Create the buttons for foods according to the database.
	 *
	 */

	public void loadImage(String categorie) {
		catButtonList.clear();

		if (catButtonList.size() == 0) {
			int i = 0;
			int p=0;
			for (Menu item : dbm.getFoodname(categorie)) {

				Button button = new Button(item.getName());
				button.setStyle("-fx-background-color: #2596be; -fx-border-color: white;  -fx-text-fill: white;-fx-content-display: top;"  );
				Image image = dbm.getFoodUrl(categorie).getItems().get(i);
				i++;
				ImageView view = new ImageView(image);
				view.setFitHeight(50);
				view.setFitWidth(80);
				button.setPrefSize(120, 70);
				button.setWrapText(true);
				button.setGraphic(view);
				button.setUserData(item);
				button.setOnMouseClicked(new EventHandler<MouseEvent>() {
					Menu m=(Menu) button.getUserData();
					@Override
					public void handle(MouseEvent event) {
						OrderViewController ov =new OrderViewController();
						o.addOrder(m);
						selectedButton = button;
					}
				});

				catButtonList.add(button);





			}
			LinkedHashSet<Button> hs=new LinkedHashSet<>(catButtonList);
			ArrayList<Button> ls=new ArrayList<>(hs);
            categorie cat=new categorie(ls,categorie);

			buttonList.add(cat);


		}

	}


	}

