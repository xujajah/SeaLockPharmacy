package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class Customer {
	private IntegerProperty custId = new SimpleIntegerProperty(this, "custId",0);
	private StringProperty custName = new SimpleStringProperty(this,"custName","");
	private StringProperty custIdentity = new SimpleStringProperty(this,"custIdentity","");
	private StringProperty custAddress = new SimpleStringProperty(this,"custAddress","");
//	private ObjectProperty<Province> custProvince = new SimpleObjectProperty<Province>(this,"custProvince",new Province());
	private ObjectProperty<Region> custRegion = new SimpleObjectProperty<Region>(this,"custRegion",new Region());
	private StringProperty custEmail = new SimpleStringProperty(this,"custEmail","");
	private StringProperty custPhone = new SimpleStringProperty(this,"custPhone","");
	private ObjectProperty<Date> custDOB = new SimpleObjectProperty<Date>(this,"custDOB", new Date());
	private ObjectProperty<Employee> custEmployee = new SimpleObjectProperty<Employee>(this,"custEmployee",new Employee());
	private BooleanProperty custActive = new SimpleBooleanProperty(this,"custActive",false);

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getCustId() {
		return custId.get();
	}

	@NotNull
	public String getCustName() {
		return custName.get();
	}

	@NotNull
	public String getCustIdentity() {
		return custIdentity.get();
	}

	public String getCustAddress() {
		return custAddress.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "custRegion")
	public Region getCustRegion() {
		return custRegion.get();
	}

	public String getCustEmail() {
		return custEmail.get();
	}

	@NotNull
	public String getCustPhone() {
		return custPhone.get();
	}
	
	@Temporal (TemporalType.DATE)
	public Date getCustDOB() {
		return custDOB.get();
	}

	@ManyToOne
	@JoinColumn(name = "custEmployee")
	public Employee getCustEmployee() {
		return custEmployee.get();
	}
	
	@NotNull
	public boolean getCustActive() {
		return custActive.get();
	}

	//getProperty Methods
	
	public IntegerProperty custIdProperty() {
		return custId;
	}
	public StringProperty custNameProperty() {
		return custName;
	}
	public StringProperty custIdentityProperty() {
		return custIdentity;
	}
	public StringProperty custAddressProperty() {
		return custAddress;
	}
	public ObjectProperty<Region> custRegionProperty() {
		return custRegion;
	}
	public StringProperty custEmailProperty() {
		return custEmail;
	}
	public StringProperty custPhoneProperty() {
		return custPhone;
	}
	public ObjectProperty<Date> custDOBProperty(){
		return custDOB;
	}
	public ObjectProperty<Employee> custEmployeeProperty(){
		return custEmployee;
	}
	public BooleanProperty custActiveProperty() {
		return custActive;
	}

	//Setters

	public void setCustId(int custId) {
		this.custId.set(custId);
	}
	public void setCustName(String custName) {
		this.custName.set(custName);
	}
	public void setCustIdentity(String custIdentity) {
		this.custIdentity.set(custIdentity);
	}
	public void setCustAddress(String custAddress) {
		this.custAddress.set(custAddress);
	}
	public void setCustRegion(Region custRegion) {
		this.custRegion.set(custRegion);
	}
	public void setCustEmail(String custEmail) {
		this.custEmail.set(custEmail);
	}
	public void setCustPhone(String custPhone) {
		this.custPhone.set(custPhone);
	}
	public void setCustDOB(Date custDOB) {
		this.custDOB.set(custDOB);
	}
	public void setCustEmployee(Employee custEmployee) {
		this.custEmployee.set(custEmployee);
	}
	public void setCustActive(boolean custActive) {
		this.custActive.set(custActive);
	}
	
	@Override
	public String toString() {
		return getCustName();
	}
}


