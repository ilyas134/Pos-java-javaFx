package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * An object that represents a Menu, consist of name and price attribute. Also
 * contain method for accessing private attributes.
 * 
 *
 *
 */

public class Menu {
	
	private String name;
	private int price;
	private int rprice;
	private String createdDate;
	private String table;
	private String categorie;
	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	public String getCategorie() {
		return categorie;
	}

	/**
	 * Constructor for Menu class.
	 * 
	 * @param name
	 * @param price
	 */

	public Menu(String name, int price) {
		this.name = name;
		this.price = price;

	}
	public Menu(String name, int price,String createdDate) {
		this.name = name;
		this.price = price;
		this.createdDate=createdDate;
	}
	public Menu(String name,int price, int rprice,String createdDate) {
		this.name = name;
		this.price = price;
		this.createdDate=createdDate;
		this.rprice=rprice;

	}
	public Menu(int price,String name, int rprice,String createdDate,String categorie) {
		this.name = name;
		this.price = price;
		this.createdDate=createdDate;
		this.rprice=rprice;
		this.categorie=categorie;
	}
	public Menu(String name, int price,int rprice,String createdDate,String table) {
		this.name = name;
		this.price = price;
		this.createdDate=createdDate;
		this.rprice=rprice;
		this.table=table;
	}
	public Menu(String name, int price,String createdDate,String table) {
		this.name = name;
		this.price = price;
		this.createdDate=createdDate;
		this.table=table;
	}
	/**
	 * Get Menu name.
	 * 
	 * @return menu name as String
	 */
	public String getName() {
		return this.name;
	}

	public int getRprice() {
		return rprice;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Get Menu price.
	 * 
	 * @return menu price as int
	 */
	public int getPrice() {
		return this.price;
	}

	public String getTable() {
		return table;
	}

	/**
	 * Equal method for Menu Class. Override the method for containsKey to work
	 * with Menu.
	 *
	 * @return true if name matches
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Menu)) {
			return false;
		}
		Menu o = (Menu) obj;
		return Objects.equals(this.name, o.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.price,this.createdDate);
	}

}
