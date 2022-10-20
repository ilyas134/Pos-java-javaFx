package controller;

import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


import application.Tableview;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import database.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;


import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.categorie;
//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.printing.PDFPageable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import org.apache.pdfbox.printing.PDFPrintable;
import util.ImageFactory;
import util.ScreenController;
import util.UserManager;

/**
 * OrderController(manager/employee) contains method for handling all event
 * receive from the UserInterface. Contains method for viewing and ordering
 * customer orders. (With help form TA for spacing nodes)
 *
 *
 *
 */
public class OrderViewController   implements java.util.Observer {

	private Button exit;
	@FXML
	private Button back;
	@FXML
	private Button actualiser;


	@FXML
	private Button order;

	@FXML
	private TextField total;

	@FXML
	private FlowPane foodpane;

	@FXML
	private FlowPane Drinkpane;

	@FXML
	private FlowPane Desserte;

	@FXML
	private FlowPane Plats;

	@FXML
	private FlowPane Salades;

	@FXML
	private FlowPane dejeuner;

	@FXML
	private FlowPane crepes;

	@FXML
	private FlowPane burger;

	@FXML
	private TabPane tab;

	@FXML
	private FlowPane Tacos;

	@FXML
	private FlowPane Jus;

	@FXML
	private Button remove;

	@FXML
	private Button clear;

	@FXML
	private TextArea display;

	@FXML
	private ScrollPane scrol2;

	@FXML
	private TextArea display2;

	@FXML
	private Button mchange;

	@FXML
	private Button imprimer;

	@FXML
	private TextField mprice;
	@FXML
	private TextField serveur;
	@FXML
	private ImageView act;
	@FXML
	private Label table;
	@FXML
	private Alert alert;
 public String name;
	private static String tablenumber;
	private static List<Menu> foods;
	private static List<Menu> drinks;
	private UserManager um = UserManager.getInstance();
	private Order o = Order.getInstance();
	private static DBManager dbm = DBManager.getInstance();
	private boolean admin = um.isAdmin();
	private int tmpTotal;
	ImageFactory img;
	private ImageFactory instance = ImageFactory.getInstance();
	@FXML
	public void initialize() {

		o.addObserver(this);
		createTab();
		act.setOnMouseClicked(event->actualiser());
		String text="table "+ tablenumber;
	     table.setText(text);

	}

	/**
	 * Overridden method from java.util.Observer to set the display everytime a menu
	 * button is pressed.
	 */
	@Override
	public void update(Observable observable, Object arg) {
		setDisplay();
	}

	/**
	 * Handler for order button. When event receive orders are sent out to the
	 * database.
	 *
	 * @param event
	 */
	public void command(){
		if (o.getOrders().isEmpty()) {
			alert = new Alert(AlertType.ERROR, "Must order atleast one item!", ButtonType.OK);
			alert.show();
		}

		else {
			String temp = "" + (o.getTotal() + tmpTotal);
			alert = new Alert(AlertType.CONFIRMATION, "Are you sure to order?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					dbm.orderToDB(tablenumber, o.getOrders());
					setDisplay2();
					o.clearOrders();
					dbm.insertToSummary(dbm.getDBOrders("table" + tablenumber),tablenumber);

					try {
						dbm.insertTomoney(dbm.getDBOorders("summary"));

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

			});


		}

	}
	public void orderButtonHandler(MouseEvent event) {
		command();
		o.clearOrders();
		System.out.println(tablenumber);
		dbm.clearTable("table" + tablenumber);
	}
	@FXML
	void changeButtonHandler(MouseEvent event) {
		String txt=mprice.getText();

		Map<Menu, Integer> temp = new LinkedHashMap<>();
		temp=o.getOrders();
		List<String> temp2 = new ArrayList<>();
		temp.forEach((k, v) -> temp2.add(k.getName()));
		ChoiceDialog<String> dialog = new ChoiceDialog<>("SELECT", temp2);
		dialog.setTitle("change price");
		dialog.setHeaderText("Please select an order wish to change price");
		dialog.setContentText("Choose which to change price:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result.get().equals("SELECT")) {
				alert = new Alert(AlertType.ERROR, "Please select something!", ButtonType.OK);
				alert.show();
			}
			else {

				alert = new Alert(AlertType.INFORMATION, result.get() + " is changed.", ButtonType.OK);
				Map<Menu, Integer> finalTemp = temp;
				alert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {

						for (Map.Entry<Menu, Integer> x : finalTemp.entrySet()) {
							if(x.getKey().getName().equals(result.get())){
								x.getKey().setPrice(Integer.parseInt(txt));
							}

						}
					}
				});
			}


			update(new Observable () , new Object() );}
	}





	/**
	 * Handler for clear button. When event receive current orders from
	 * Map<Menu,Integer> is removed.
	 *
	 * @param event
	 */
	public void clearButtonHandler(MouseEvent event) {
		o.clearOrders();
	}

	/**
	 * Handler for back button. When event receive the CS table scene is shown.
	 *
	 */
	public void backButtonHandler(ActionEvent event) {
		o.clearOrders();
		dbm.clearTable("table" + tablenumber);

		ScreenController.switchWindow((Stage) back.getScene().getWindow(), new Tableview());
	 


	}

	/**
	 * Handler for back button. When event receive the CS table scene is shown.
	 *
	 */




	/**
	 * Static method for scene before opening this scene to get the button text and
	 * set as table number.
	 *
	 * @param buttonText
	 */
	public static void setTable(String buttonText) {
		tablenumber = buttonText;
	}

	/**
	 * Static method for scene before opening this scene to get list of menu names
	 * and set the List<Menu> attribute above.
	 *
	 * @param List
	 *            of menu names List<Menu>
	 */
	public static void setMenu(List<Menu> arg, List<Menu> arg2) {
		foods = arg;
		drinks = arg2;
	}

	/*
	 * Private method for removeButtonHandler. This method do all the jobs.
	 */

	/**
	 * Set the current total
	 */
	private void setTotal() {
		String temp = "" + (o.getTotal() + tmpTotal);
		total.setText(temp);
	}

	/**
	 * Set the temporary total attribute which is use to display the current total
	 */
	private void setTempTotal(Map<Menu, Integer> map) {
		tmpTotal = o.getTotal(map);
	}

	/**
	 * Set the display properties
	 */
	private void setDisplayProp() {
		display.setDisable(true);
		display.setText(tablenumber);
		display2.setDisable(true);
		setDisplay2();
		setTotal();
	}

	/**
	 * Set the top display in the UI
	 */
	private void setDisplay() {
		String text = o.orderToText(o.getOrders());
		display.setText(text);
		setTotal();
	}

	/**
	 * Set the lower display in the UI
	 */
	private void setDisplay2() {
		Map<Menu, Integer> temp = o.getDBOrders(tablenumber);
		String text = o.orderTooText(temp);

		display2.setText(text);
		scrol2.setContent(display2);



		setTempTotal(temp);

	}

    public void imprimer(ActionEvent actionEvent) throws IOException, DocumentException, PrinterException {
		command();

		Map<Menu, Integer> temp = o.getDBOrders(tablenumber);
		String text = o.ordeerToooText(temp);
		Rectangle two = new Rectangle(306,620);
Document doc =new Document(two);
PdfWriter.getInstance(doc,new FileOutputStream("C:\\Test\\recu.pdf"));
doc.open();
Image img=Image.getInstance("C:\\logo\\smile.jpg");
img.scaleAbsoluteWidth(200);
img.scaleAbsoluteHeight(92);
img.setAlignment(Image.ALIGN_CENTER);
doc.add(new Paragraph("Ticket"));
		doc.add(new Paragraph("-------------------------------------------------------"));
		doc.add(img);
		doc.add(new Paragraph("-------------------------------------------------------"));
		doc.add(new Paragraph("Seveur:"+serveur.getText()));
		Map.Entry<Menu,Integer> entry = temp.entrySet().iterator().next();
		Menu menu=entry.getKey();
		doc.add(new Paragraph("date:"+menu.getCreatedDate()));
		doc.add(new Paragraph("Tel: 0702264637  / 0674725637"));
		doc.add(new Paragraph("Instagram: Smile_saidia"));
		doc.add(new Paragraph("Facebook: SmileSaidia"));
		doc.add(new Paragraph("-------------------------------------------------------"));
		doc.add(new Paragraph("             "));

		doc.add(new Paragraph(text));
		doc.setHtmlStyleClass(" font-family: cursive;");

		doc.add(new Paragraph("-------------------------------------------------------"));
		doc.add(new Paragraph("          Total:"+o.getTotal(temp)+"Dh"));
		doc.add(new Paragraph("-------------------------------------------------------"));
		doc.add(new Paragraph("          merci pour votre visite"));


		doc.close();
		Desktop.getDesktop().open(new File("C:\\Test\\recu.pdf"));
		String filename = "C:\\Test\\recu.pdf";
		PDDocument document = PDDocument.load(new File (filename));

		//takes standard printer defined by OS
		PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();

		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPageable(new PDFPageable(document));
		job.setPageable(new PDFPageable(document));

		// define custom paper
		Paper paper = new Paper();
		paper.setSize(306, 396); // 1/72 inch
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

		// custom page format
		PageFormat pageFormat = new PageFormat();
		pageFormat.setPaper(paper);

		// override the page format
		Book book = new Book();
		// append all pages
		book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
		job.setPageable(book);
		job.setPrintService(myPrintService);
		job.print();
		o.clearOrders();
		dbm.clearTable("table" + tablenumber);


	}
    public void actualiser( ) {
		tab.getTabs().clear();
		img=new ImageFactory();
		instance=img;
		List <categorie> categ=img.getButton();
		List<String> temp = dbm.getDBTTables();
		for (String tablenum : temp) {
			img.loadImage(tablenum);
		}

		for (int i =0;i<categ.size();i++ ) {

			VBox cat = new VBox();
			Tab tab1 = new Tab(categ.get(i).getCategorie());
			FlowPane fpane=new FlowPane();

			categ.get(i).getCatButtonList().forEach(x->fpane.getChildren().add(x)


			);

			cat.getChildren().add(fpane);
			tab1.setContent(cat);

			tab.getTabs().addAll(tab1);
		}
    }
	public void createTab() {

		List <categorie> categ=instance.getButton();


		for (int i =0;i<categ.size();i++ ) {

			VBox cat = new VBox();
			Tab tab1 = new Tab(categ.get(i).getCategorie());
			FlowPane fpane=new FlowPane();

			categ.get(i).getCatButtonList().forEach(x->fpane.getChildren().add(x)


			);

			cat.getChildren().add(fpane);
			ScrollPane scr=new ScrollPane(cat);
			tab1.setContent(scr);

			tab.getTabs().addAll(tab1);
		}
	}


}
