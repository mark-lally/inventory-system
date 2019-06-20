package com.MarkLally.InventorySystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column()
	@NotBlank(message="Must enter a valid name")
	private String name;
	
	@Column
	@Min(value=0, message="Price must be greater or equal to 0!")
	@Max(value=2147483647, message="Price cannot be that high")
	private int price;
	
	@Column
	@NotBlank(message="Must enter a valid category")
	private String category;
	
	// A column to hold insertion timestamp for this object. Defaults to current timeStamp
	// Will be used in 'return last 5 items added' custom query.
	@Column(insertable=false, updatable=false, nullable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public Item () {}

	public Item(int id, String name, int price, String category) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.category = category;
		this.date = new Date();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + ", date=" + date
				+ "]";
	}
}
