package database;


import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.Date;

import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import model.*;
import org.mindrot.jbcrypt.BCrypt;

import util.PropertyManager;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * DBManager contains method for managing data on database. Also associate with
 * classes that use the database. With help from BCrypt all password are
 * encrypted.
 *
 *
 *
 */
public class DBManager {
	private PropertyManager pm = PropertyManager.getInstance();
	private static DBManager instance;
	private Connection connection;
	private String DB_URL = pm.getProperty("jdbc:mysql://localhost/restaurant");
	private String USER = pm.getProperty("root");
	private String PASS = pm.getProperty("");
	private String sqlCommand;
	private String sqlCommand2;
	private String sqlCommand3;
	private int tmpTotal;
	/**
	 * Private constructor for DBManger. Getting the connection from the database.
	 */
	private DBManager() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restaurant", "root","");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
	}

	/**
	 * Method for getting the instance of DBManager with the condition if the
	 * instance is null, create new instance.
	 *
	 * @return instance of the DBManager
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	/**
	 * Method for getting data from the database to check the Login's input.
	 *
	 * @param username
	 *            from Login's input
	 * @param password
	 *            from Login's input
	 * @return 2 for manager, 1 for normal employee, 0 = wrong password, -1 = user
	 *         doesn't exists
	 */

	public int login(String user, String pass) {
		sqlCommand = "SELECT * FROM user WHERE name = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();
			String dbPass = "";
			if (rs.next()) {
				dbPass = rs.getString("password");
			}
			if (BCrypt.checkpw(pass, dbPass)) {
				return rs.getInt("access type");
			}
			if (!dbPass.equals("")) {
				return 0;
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} catch (StringIndexOutOfBoundsException ex) {
			return -1;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return -1;

	}
	public int login(String pass) {
		String  user="yassine";
		sqlCommand = "SELECT * FROM user WHERE name= ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();
			String dbPass = "";
			if (rs.next()) {
				dbPass = rs.getString("password");
			}
			if (BCrypt.checkpw(pass, dbPass)) {
				System.out.println(rs.getInt("access type"));
				return rs.getInt("access type");

			}
			if (!dbPass.equals("")) {
				return 0;
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} catch (StringIndexOutOfBoundsException ex) {
			return -1;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return -1;

	}
	/**
	 * Method for inserting data(new user's data) to the database. The access type
	 * is set to 1 by default but can be change later on.
	 *
	 * @param username
	 *            from SignUp window
	 * @param password
	 *            from SignUp window
	 */
	public void signUp(String user, String pass) {
		sqlCommand = "INSERT INTO `user` (`name`, `password`, `access type`) VALUES (?, ?, ?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, user);
			String hashpw = BCrypt.hashpw(pass, BCrypt.gensalt());
			stmt.setString(2, hashpw);
			stmt.setInt(3, 1);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	/**
	 * Method for getting data from the database to check whether if the username
	 * inputed has already exist or not.
	 *
	 * @param username
	 *            from SignUp window
	 * @return false if username match, true if no match
	 */
	public boolean checkUser(String user) {
		sqlCommand = "SELECT * FROM User WHERE name = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();
			int dbInt = 0;
			if (rs.next()) {
				dbInt = rs.getInt("access type");
			}
			if (dbInt == 1 || dbInt == 2) {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return true;
	}
	/**
	 * Method for getting all the food urls from the database.
	 *
	 * @param table
	 * @return List<String> of food urls
	 */

	public ListView<Image> getFoodUrl(String table) {
		ListView<Image> tmpUrl = new ListView();
		sqlCommand = "SELECT * FROM " + table;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				InputStream  blob = rs.getBinaryStream("url");

				if (blob != null && blob.available() > 1) {

					Image imge = new Image(blob);

					tmpUrl.getItems().add(imge);
				}




			}
		} catch (SQLException | IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tmpUrl;
	}
	public Image getFoodUrl(String table,String name) {
		Image imge=null;
		sqlCommand = "SELECT * FROM " + table+ " WHERE name = ?";
		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				InputStream  blob = rs.getBinaryStream("url");

				if (blob != null && blob.available() > 1) {

					 imge = new Image(blob);


				}




			}
		} catch (SQLException | IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return imge;
	}

	/**
	 * Method for getting data from the database which are food names and prices to
	 * create a Menu object.
	 *
	 * @param tablename
	 * @return List<Menu> of food names
	 */
	public List<Menu> getFoodname(String foodkind) {
		List<Menu> tmpFoodname = new ArrayList<>();

		sqlCommand = "SELECT * FROM " + foodkind;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String text = rs.getString("name");
				int price = rs.getInt("price");
				int rprice=rs.getInt("rprice");

				Date date =new Date();
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
				String timme =formater.format(date) ;
				String categorie=rs.getString("categorie");

				Menu mn = new Menu( price,text,rprice,timme,categorie);

				tmpFoodname.add(mn);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tmpFoodname;
	}


	/**
	 * Method for inserting the data into the database.
	 *  @param tablename
	 *            in the database
	 * @param food's
	 *            name
	 * @param food's
	 *            price
	 * @param i
	 * @param url
	 */
	public void insertTo(String foodtable, String name, Integer price, Integer Rprice, String url) {
		sqlCommand = "INSERT INTO `" + foodtable + "` (`name`, `price`,`rprice`, `url`,`categorie`) VALUES (?, ?,?, ?,?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, name);
			stmt.setInt(2, price);
			stmt.setInt(3, Rprice);

			InputStream img = new FileInputStream(new File(url));
			stmt.setBlob(4, img);
			stmt.setString(5, foodtable);
			stmt.executeUpdate();
		} catch (SQLException | FileNotFoundException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for checking table existence in database.
	 *
	 * @param table
	 *            number
	 * @return true if table exist, false if not
	 */
	public boolean checkTable(String tableNumber) {
		DatabaseMetaData dbm = null;
		String tmpTable = "table" + tableNumber;
		try {
			dbm = connection.getMetaData();
			ResultSet table = dbm.getTables(null, null, tmpTable, null);
			if (table.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	public boolean checkkTable(String tableName) {
		DatabaseMetaData dbm = null;
		String tmpTable =tableName;
		try {
			dbm = connection.getMetaData();
			ResultSet table = dbm.getTables(null, null, tmpTable, null);
			if (table.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	/**
	 * Method for creating a new table in database.
	 *
	 * @param table
	 *            number
	 */
	public void createTable(String tableNumber) {
		String table = "table" + tableNumber;
		sqlCommand = "CREATE TABLE " + table + "(name VARCHAR (255), price INT(11), quantity INT(11),date VARCHAR (255),rprice INT(11))";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void creeateTable(String tablename) {
		String table =tablename;
		sqlCommand = "CREATE TABLE " + table + "(name VARCHAR (255), price INT(11), url LONGBLOB,rprice INT(11),categorie VARCHAR (255))";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	/**
	 * Method for getting list of users from the database.
	 *
	 * @return List<User> of users
	 */
	public List<User> getDBUser() {
		List<User> tmpUser = new ArrayList<>();
		sqlCommand = "SELECT * FROM " + "User";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String text = rs.getString("name");
				User user = new User(text, PrivilegeEnum.USER);
				tmpUser.add(user);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tmpUser;
	}
	public List<Table> getDBmoney() {
		List<Table> tmpUser = new ArrayList<>();
		sqlCommand = "SELECT * FROM " + "money";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String table = rs.getString("table");
				int total = rs.getInt("total");
				String date = rs.getString("date");
				String etat = rs.getString("etat");
				Table tab = new Table(table, date,etat,total);
				tmpUser.add(tab);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tmpUser;
	}
	public List<Table> getDBmoney(String filter) {
		List<Table> tmpUser = new ArrayList<>();
		if(filter.equals("Ordres par jour")){

            Date daate =new Date();
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
			String timme =formater.format(daate) ;
			String time=timme.substring(0,10);
			sqlCommand = "SELECT * FROM " + "money";
			PreparedStatement stmt = null;
			try {
				stmt = connection.prepareStatement(sqlCommand);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					String table = rs.getString("table");
					int total = rs.getInt("total");
					String date = rs.getString("date");
					String etat = rs.getString("etat");

					if(date.substring(0,10).equals(time)){


						Table tab = new Table(table, date,etat,total);
						tmpUser.add(tab);
					}



				}
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}else if(filter.equals("Ordres par semaine")){
			Date daate =new Date();
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
			String timme =formater.format(daate) ;
			String time=timme.substring(2,10);
            int i=0;
			sqlCommand = "SELECT * FROM " + "money";
			PreparedStatement stmt = null;
			try {
				stmt = connection.prepareStatement(sqlCommand);
				ResultSet rs = stmt.executeQuery();
				int tt=Integer.parseInt(timme.substring(0,2))-7;

				int day=0;
				int month=Integer.parseInt(timme.substring(3,5));
				int year=Integer.parseInt(timme.substring(6,10));

				if (tt<1){
                  if(month>1){
					  month=month-1;
					  if (month %2!=0) {

						  day=31-(-1*tt);

					  }
					  else if(month %2==0){
						  day=30-(-1*tt);
					  }
				  }
                  else if(month==1){
                  	year=year-1;
                  	month=12;
                  	day=31-(-1*tt);
				  }
                  else if(month==2){
                  	if(year%4==0){

						day=29-(-1*tt);
					}else{
						day=28-(-1*tt);
					}
				  }


				}
				while (rs.next()) {
					String table = rs.getString("table");
					int total = rs.getInt("total");
					String date = rs.getString("date");
					String etat = rs.getString("etat");
					boolean b = Integer.parseInt(date.substring(3, 5)) > month;
					boolean a = Integer.parseInt(date.substring(6, 10)) > year;
					if(((Integer.parseInt(date.substring(0,2))>=day) && Integer.parseInt(date.substring(3, 5)) >= month
							&& (Integer.parseInt(date.substring(6,10))>=year)) || (b && Integer.parseInt(date.substring(6, 10)) >= year) || a){
						Table tab = new Table(table, date,etat,total);
						tmpUser.add(tab);
					}



				}
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}

		return tmpUser;
	}
	public  Map<String, String> getDBmmoney() {
		Map<String, String> temp = new LinkedHashMap<>();
		sqlCommand = "SELECT * FROM " + "money";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String table = rs.getString("table");

				String date = rs.getString("date");
			 ;

				temp.put(date,table);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return temp;
	}
	/**
	 * Method for inserting current orders into the requested table in database.
	 *
	 * @param tablenumber
	 * @param Map<Menu,Integer>
	 *            of orders
	 */
	public void orderToDB(String tableNumber, Map<Menu, Integer> map) {
		String tmpTable = "table" + tableNumber;
		sqlCommand = "INSERT INTO `" + tmpTable + "` (`name`, `price`, `quantity`,`date`,`rprice`) VALUES (?, ?, ?,?,?)";
		PreparedStatement stmt = null;
		try {
			for (Map.Entry<Menu, Integer> order : map.entrySet()) {
				stmt = connection.prepareStatement(sqlCommand);
				String name = order.getKey().getName();
				int price = order.getKey().getPrice();
				int rprice = order.getKey().getRprice();
				int qty = order.getValue();
				Date date =new Date();
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
				String timme =formater.format(date) ;
				order.getKey().setCreatedDate(timme);
				String  time =order.getKey().getCreatedDate();


				stmt.setString(1, name);
				stmt.setInt(2, price);
				stmt.setInt(3, qty);
				stmt.setObject(4, time);
				stmt.setObject(5, rprice);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for getting all orders and from the wanted table in database and
	 * collect them as Map<Menu,Integer>.
	 *
	 * @param tableNumber
	 * @return Map<Menu,Integer> of orders
	 */
	public Map<Menu, Integer> getDBOrders(String tableNumber) {
		Map<Menu, Integer> temp = new LinkedHashMap<>();
		Map<Menu, Integer> temp2 = new LinkedHashMap<>();
		sqlCommand = "SELECT * FROM " + tableNumber;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			System.out.println("hell"+tableNumber);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int qty = rs.getInt("quantity");
				String date = rs.getString("date");

				int rprice=rs.getInt("rprice");



				Date timme =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").parse(date);

				Menu menu = new Menu(name, price,rprice,date);
				menu.setCreatedDate(date);
				if (!temp.containsKey(menu)) {
					temp.put(menu, qty);

				} else {
					temp.put(menu, temp.get(menu) + qty);

				}
			}
			for (Map.Entry<Menu, Integer> x : temp.entrySet()) {
				if (x.getValue() > 0)
					temp2.put(x.getKey(), x.getValue());

			}
		} catch (SQLException | ParseException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return temp2;
	}

	public Map<Menu, Integer> getDBOorders(String tableNumber) {
		Map<Menu, Integer> temp = new LinkedHashMap<>();
		Map<Menu, Integer> temp2 = new LinkedHashMap<>();
		sqlCommand = "SELECT * FROM " + tableNumber;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int qty = rs.getInt("quantity");
				String date = rs.getString("date");
				String table=rs.getString("table");
				int rprice=rs.getInt("rprice");



				Date timme =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").parse(date);

				Menu menu = new Menu(name, price,rprice,date,table);
				menu.setCreatedDate(date);
				if (!temp.containsKey(menu)) {
					temp.put(menu, qty);

				} else {
					temp.put(menu, temp.get(menu) + qty);

				}
			}
			for (Map.Entry<Menu, Integer> x : temp.entrySet()) {
				if (x.getValue() > 0)
					temp2.put(x.getKey(), x.getValue());

			}
		} catch (SQLException | ParseException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return temp2;
	}
	public Map<Menu, Integer> getDBbOorders(String tableNumber,String tablee,String datee) {
		Map<Menu, Integer> temp = new LinkedHashMap<>();
		Map<Menu, Integer> temp2 = new LinkedHashMap<>();
		sqlCommand = "SELECT * FROM " + tableNumber;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int qty = rs.getInt("quantity");
				String date = rs.getString("date");
				String table=rs.getString("table");

				Menu menu = new Menu(name, price,date,table);

				if (!temp.containsKey(menu)) {
					temp.put(menu, qty);

				} else {
					temp.put(menu, temp.get(menu) + qty);

				}
			}
			for (Map.Entry<Menu, Integer> x : temp.entrySet()) {
				if ((x.getKey().getCreatedDate().equals(datee)) &&(x.getKey().getTable().equals(tablee)))
					temp2.put(x.getKey(), x.getValue());

			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return temp2;
	}



	public Map<Menu, Integer> getDBOOrders(String tableNumber) throws SQLException {
		Map<Menu, Integer> temp = new LinkedHashMap<>();
		Map<Menu, Integer> temp2 = new LinkedHashMap<>();
		sqlCommand = "SELECT * FROM " + tableNumber;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String tab=rs.getString("table");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int qty = rs.getInt("quantity");
				String date = rs.getString("date");

				Menu menu = new Menu(name, price, date,tab);
				menu.setCreatedDate(date);
				if (temp.containsKey(menu)) {
					temp.put(menu, temp.get(menu) + qty);
				}else {
					temp.put(menu, qty);
				}
			}
			for (Map.Entry<Menu, Integer> x : temp.entrySet()) {
				if (x.getValue() > 0)
					temp2.put(x.getKey(), x.getValue());

			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return temp2;
	}

	public Map<String, Integer>  getDBsstat () throws SQLException {

		Map<String, Integer> temp = new LinkedHashMap<>();

		sqlCommand = "SELECT * FROM `stat` " ;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String date = rs.getString("date");


				int income = rs.getInt("income");

				temp.put(date,income);
			}



		} finally {

		}
		return temp;
	}
	public Map<String, Integer>  getDBsstat (String m,String y) throws SQLException {

		Map<String, Integer> temp = new LinkedHashMap<>();

		sqlCommand = "SELECT * FROM `stat` " ;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String date = rs.getString("date");


				int income = rs.getInt("income");

				if(date.substring(3,5).equals(m) && date.substring(6,10).equals(y)){
					temp.put(date,income);
				}

			}



		} finally {

		}
		return temp;
	}
	public String getDBsstat (String m) throws SQLException {

		String temp="";

		sqlCommand = "SELECT * FROM `stat` " ;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String date = rs.getString("date");


				int income = rs.getInt("incomee");

				if(date.equals(m)){
					int a=income*10;
					double b=100;
					Double s=((a/b)+500);
					float l=(float) (156/100);


					temp="daily salary:"  +s;
				}

			}



		} finally {

		}
		return temp;
	}
	public ArrayList<Integer> getDBmm (String m) throws SQLException {

		ArrayList <Integer> temp= new ArrayList<>();
        int s=0;
        int l=0;
        int n=0;
		sqlCommand = "SELECT * FROM `money` " ;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String date = rs.getString("date");
				String eta =rs.getString("etat");

				int Total = rs.getInt("total");

				if(date.substring(0,10).equals(m) && eta.equals("cash")){
					 s+=Total;

				}

				if(date.substring(0,10).equals(m) && eta.equals("carte")){
					l+=Total;

				}
				if(date.substring(0,10).equals(m) && eta.equals("credit")){
					n+=Total;

				}

			}
            temp.add(s);
			temp.add(l);
			temp.add(n);

		} finally {

		}
		return temp;
	}
	/**
	 * Method for clearing all records in the wanted database table.
	 *
	 * @param table
	 *            requested
	 */
	public void clearTable(String table) {
		PreparedStatement stmt = null;
		sqlCommand = "DELETE FROM " + table;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.execute();
		} catch (SQLException ex) {
			System.out.println(ex);
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for inserting items, which is going to be paid, to the table in
	 * database with specific tbale number.
	 *
	 * @param tablenumber
	 * @param list
	 *            of the table items
	 */
	public void insertToSummary(Map<Menu, Integer> map,String tablenumber) {
		sqlCommand = "INSERT INTO `summary` (`name`, `price`, `quantity`,`date`,`table`,`rprice`) VALUES (?, ?, ?, ?,?,?)";
		PreparedStatement stmt = null;
		try {
			for (Map.Entry<Menu, Integer> order : map.entrySet()) {
				stmt = connection.prepareStatement(sqlCommand);
				String name = order.getKey().getName();
				int price = order.getKey().getPrice();
				int qty = order.getValue();
				String  time =order.getKey().getCreatedDate();
				String tab="Table"+tablenumber;
				int rprice=order.getKey().getRprice();
				stmt.setString(1, name);
				stmt.setInt(2, price);
				stmt.setInt(3, qty);
				stmt.setString(4, time);
				stmt.setString(5, tab);
				stmt.setInt(6, rprice);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void insertTostat (Map<Menu, Integer> map) throws SQLException {
		Map<String, Integer> temp2 = new LinkedHashMap<>();
		temp2=getDBsstat();
		sqlCommand = "INSERT  INTO `stat` (`date`,`income`,`incomee`) VALUES (?,?,?)";
		sqlCommand2 = "UPDATE  `stat` set income=?,incomee=? where date=?";
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ArrayList check = new ArrayList();
		check.add("");


		Map<Menu, Integer> temp = new LinkedHashMap<>();

		int income;
		int expense;
		try {
			for (Map.Entry<Menu, Integer> order : map.entrySet()) {

				stmt = connection.prepareStatement(sqlCommand);
				stmt1 = connection.prepareStatement(sqlCommand2);
				String date = order.getKey().getCreatedDate().substring(0,10);

				for (Map.Entry<Menu, Integer> x : map.entrySet()) {

					if (x.getKey().getCreatedDate().substring(0,10).equals(date))
						temp.put(x.getKey(), x.getValue());

				}
				income =(Order.getInstance().getTotal(temp) + tmpTotal);
				expense=(Order.getInstance().getTotalexpense(temp) + tmpTotal);
				if ((!(check.contains(date)) && !(temp2.containsValue(income) ) &&  !(temp2.containsKey(date) ))  ){

					stmt.setString(1, date);
					stmt.setInt(2, income-expense);
					stmt.setInt(3, income);

					stmt.executeUpdate();
				}
				if((!(check.contains(date)) && !(temp2.containsValue(income) ) &&  (temp2.containsKey(date) ))){
					stmt1.setInt(1, income-expense);
					stmt1.setString(3, date);
					stmt1.setInt(2, income);
					stmt1.executeUpdate();
				}

				check.add(date);
				temp.clear();


			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

	}
	public void insertTomoney (Map<Menu, Integer> map) throws SQLException {
		Map<String, String> temp2 = new LinkedHashMap<>();
		temp2=getDBmmoney();
		int total=0;
		String etat="";
		sqlCommand = "INSERT  INTO `money` (`table`,`total`,`date`,`etat`) VALUES (?,?,?,?)";

		PreparedStatement stmt = null;

		ArrayList check = new ArrayList();
		check.add("");


		Map<Menu, Integer> temp = new LinkedHashMap<>();



		try {
			for (Map.Entry<Menu, Integer> order : map.entrySet()) {

				stmt = connection.prepareStatement(sqlCommand);

				String date = order.getKey().getCreatedDate();

                String table=order.getKey().getTable();

				for (Map.Entry<Menu, Integer> x : map.entrySet()) {

					if ( (x.getKey().getTable().equals(table))){
						for (Map.Entry<Menu, Integer> y : map.entrySet()) {



							if((y.getKey().getCreatedDate().equals(date))){

								temp.put(y.getKey(), y.getValue());
							}
						}


				}
				total =(Order.getInstance().getTotal(temp) + tmpTotal);




				}
				if ((!(check.contains(date)) && !(temp2.containsKey(date))     )  ){

					stmt.setString(1, table);
					stmt.setInt(2, total);
					stmt.setString(3, date);
					stmt.setString(4, etat);
					stmt.executeUpdate();

				}

				check.add(date);
				temp.clear();


			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

	}
	public void updatemoney (Table table,String etat) throws SQLException {




		sqlCommand = "UPDATE  `money` set etat=? WHERE date=?  ";

		PreparedStatement stmt = null;
		try {
				stmt = connection.prepareStatement(sqlCommand);
				String date= table.getDate();
				String tabe=table.getTable();
					stmt.setString(1, etat);

					stmt.setString(2, date);

					stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

	}




	public boolean checkDBFood(String foodName, String tableNumber) {
		String tmpTable = "table" + tableNumber;
		sqlCommand = "SELECT * FROM " + tmpTable + " WHERE name = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, foodName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return false;
	}

	/**
	 * Method for inserting data directly to the database without using the model.
	 *
	 * @param tableNumber
	 * @param Menu
	 *            wish to remove
	 */
	public void insertTo(String tableNumber, Menu menu) {
		sqlCommand = "INSERT INTO `" + tableNumber + "` (`name`, `price`, `quantity`) VALUES (?, ?, ?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, menu.getName());
			stmt.setInt(2, menu.getPrice());
			stmt.setInt(3, -1);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for inserting tablenumber into 'Tables'.
	 *
	 * @param tableNumber
	 */
	public void insertTableNumber(String tableNumber) {
		sqlCommand = "INSERT INTO `tables` (`number`) VALUES (?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, tableNumber);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void insertTableName(String tableName) {
		sqlCommand = "INSERT INTO `tablesc` (`names`) VALUES (?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, tableName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for getting all number of tables from database.
	 *
	 * @return List<String> of tables that is created
	 */
	public List<String> getDBTables() {
		List<String> tables = new ArrayList<>();
		sqlCommand = "SELECT * FROM tables";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String table = rs.getString("number");
				tables.add(table);
			}

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tables;
	}
	public List<String> getDBTTables() {
		List<String> tables = new ArrayList<>();
		sqlCommand = "SELECT * FROM tablesc";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String table = rs.getString("names");
				tables.add(table);
			}

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return tables;
	}

	/**
	 * Method for checking is there is orders in the table.
	 *
	 * @param tableNumber
	 * @return true if table is empty, false if not
	 */
	public boolean checkTableData(String tableNumber) {
		int value = 1;
		String tmpTable = "table" + tableNumber;
		sqlCommand = "SELECT * FROM " + tmpTable;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		Map<Menu, Integer> temp = getDBOrders("table" + tableNumber);
		for (Map.Entry<Menu, Integer> tmp : temp.entrySet()) {
			int tmpValue = tmp.getValue();
			value += tmpValue;
		}
		if (value == 1) {
			return true;
		}
		return false;
	}
	public boolean checkTTableData(String tableNumber) {

		String tmpTable =tableNumber;
		sqlCommand = "SELECT * FROM " + tmpTable;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return false;
	}

	/**
	 * Method for deleting requested table in database.
	 *
	 * @param tableNumber
	 */
	public void deleteTable(String tableNumber) {
		String table = "table" + tableNumber;
		sqlCommand = "DROP TABLE " + table;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void deleteTTable(String tableNumber) {
		String table =tableNumber;
		sqlCommand = "DROP TABLE " + table;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for removing requested tablenumber from 'Tables'.
	 *
	 * @param tableNumber
	 */
	public void removeTableinTables(String tableNumber) {
		sqlCommand = "DELETE FROM Tables WHERE number = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, tableNumber);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void removeTTableinTables(String tableNumber) {
		sqlCommand = "DELETE FROM Tablesc WHERE names = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, tableNumber);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Method for removing user from the database
	 *
	 * @param User
	 */
	public void removeUserDB(String name) {
		sqlCommand = "DELETE FROM User WHERE name = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, name);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void removeOrderDB(String name) {
		sqlCommand = "DELETE FROM money WHERE date = ?";
		sqlCommand2 = "DELETE FROM summary WHERE date = ?";
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, name);
			stmt.execute();
			stmt2 = connection.prepareStatement(sqlCommand2);
			stmt2.setString(1, name);
			stmt2.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void removeOrderDB() {
		sqlCommand = "DELETE FROM money WHERE true ";
		sqlCommand2 = "DELETE FROM summary WHERE true ";
		sqlCommand3 = "DELETE FROM stat WHERE true ";
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);

			stmt.execute();
			stmt2 = connection.prepareStatement(sqlCommand2);

			stmt2.execute();
			stmt3 = connection.prepareStatement(sqlCommand3);

			stmt3.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	/**
	 * Method for removing image data from the database
	 *
	 * @param foodtable
	 * @param Menu
	 */
	public void removeImage(String foodtable, Menu item) {
		sqlCommand = "DELETE FROM " + foodtable + " WHERE name = ?";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			stmt.setString(1, item.getName());
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Refresh records by getting tale rows for comparing later.
	 *
	 * @param tableNumber
	 * @return amount of rows in table
	 */
	public int refreshRecords(String tableNumber) {
		String table = "table" + tableNumber;
		int count = 0;
		sqlCommand = "SELECT * FROM " + table;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count++;
			}

		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return count;
	}

}
