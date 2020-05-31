package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Supplier {
	private IntegerProperty suppId = new SimpleIntegerProperty(this, "suppId",0);
	private StringProperty suppName = new SimpleStringProperty(this,"suppName","");
	private StringProperty suppAddress = new SimpleStringProperty(this,"suppAddress","");
	private StringProperty suppWebsite = new SimpleStringProperty(this,"suppWebsite","");
	private StringProperty suppEmail = new SimpleStringProperty(this,"suppEmail","");
	private StringProperty suppPhone = new SimpleStringProperty(this,"suppPhone","");
	private ObjectProperty<Employee> suppEmployee = new SimpleObjectProperty<Employee>(this,"suppEmployee",new Employee());
	private BooleanProperty suppActive = new SimpleBooleanProperty(this,"suppActive",false);
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getSuppId() {
		return suppId.get();
	}
	
	@NotNull
	public String getSuppName() {
		return suppName.get();
	}
	
	
	public String getSuppAddress() {
		return suppAddress.get();
	}
	
	public String getSuppWebsite() {
		return suppWebsite.get();
	}
	
	public String getSuppEmail() {
		return suppEmail.get();
	}
	
	@NotNull
	public String getSuppPhone() {
		return suppPhone.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "suppEmployee")
	public Employee getSuppEmployee() {
		return suppEmployee.get();
	}
	
	@NotNull
	public boolean getSuppActive() {
		return suppActive.get();
	}
	
	//getProperty Methods
	public IntegerProperty suppIdProperty() {
		return suppId;
	}
	public StringProperty suppNameProperty() {
		return suppName;
	}
	public StringProperty suppAddressProperty() {
		return suppAddress;
	}
	public StringProperty suppWebsiteProperty() {
		return suppWebsite;
	}
	public StringProperty suppEmailProperty() {
		return suppEmail;
	}
	public StringProperty suppPhoneProperty() {
		return suppPhone;
	}
	public ObjectProperty<Employee> suppEmployeeProperty(){
		return suppEmployee;
	}
	public BooleanProperty suppActiveProperty() {
		return suppActive;
	}

	//Setters
	
	public void setSuppId(int suppId) {
		this.suppId.set(suppId);
	}
	public void setSuppName(String suppName) {
		this.suppName.set(suppName);
	}
	public void setSuppAddress(String suppAddress) {
		this.suppAddress.set(suppAddress);
	}
	public void setSuppWebsite(String suppWebsite) {
		this.suppWebsite.set(suppWebsite);
	}
	public void setSuppEmail(String suppEmail) {
		this.suppEmail.set(suppEmail);
	}
	public void setSuppPhone(String suppPhone) {
		this.suppPhone.set(suppPhone);
	}
	public void setSuppEmployee(Employee suppEmployee) {
		this.suppEmployee.set(suppEmployee);
	}
	public void setSuppActive(boolean suppActive) {
		this.suppActive.set(suppActive);
	}
	
	@Override
	public String toString() {
		return getSuppName();
	}

}
