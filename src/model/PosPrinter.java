package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class PosPrinter {
	private IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
	private StringProperty name = new SimpleStringProperty(this,"name","");
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	
	public IntegerProperty idProperty() {
		return id;
	}
	public StringProperty nameProperty() {
		return name;
	}
	public void setId(int id) {
		this.id.set(id);
	}
	public void setName(String name) {
		this.name.set(name);
	}
	

}
