package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Company {
	private IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
	private StringProperty name = new SimpleStringProperty(this,"name","");
	private StringProperty address = new SimpleStringProperty(this,"address","");
	private StringProperty city = new SimpleStringProperty(this,"city","");
	private StringProperty phone = new SimpleStringProperty(this,"phone","");
	private StringProperty billNotes = new SimpleStringProperty(this,"billNotes","");
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getId() {
		return id.get();
	}
	
	public String getName() {
		return name.get();
	}
	public String getAddress() {
		return address.get();
	}
	public String getCity() {
		return city.get();
	}
	public String getPhone() {
		return phone.get();
	}
	@Lob
	public String getBillNotes() {
		return billNotes.get();
	}
	
	
	//property
	public IntegerProperty idProperty() {
		return id;
	}
	public StringProperty nameProperty() {
		return name;
	}
	public StringProperty addressProperty() {
		return address;
	}
	public StringProperty cityProperty() {
		return city;
	}
	public StringProperty phoneProperty() {
		return phone;
	}
	public StringProperty billNotesProperty() {
		return billNotes;
	}
	
	//setters
	public void setId(int id) {
		this.id.set(id);
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public void setAddress(String address) {
		this.address.set(address);
	}
	public void setCity(String city) {
		this.city.set(city);
	}
	public void setPhone(String phone) {
		this.phone.set(phone);
	}
	public void setBillNotes(String billNotes) {
		this.billNotes.set(billNotes);
	}
	
	

}
