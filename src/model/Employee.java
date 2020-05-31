package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Employee {
	private IntegerProperty empId = new SimpleIntegerProperty(this, "empId",0);
	private StringProperty empName = new SimpleStringProperty(this,"empName","");
	private StringProperty empIdentity = new SimpleStringProperty(this,"empIdentity","");
	private StringProperty empAddress = new SimpleStringProperty(this,"empAddress","");
	private StringProperty empDesignation = new SimpleStringProperty(this,"empDesignation","");
	private StringProperty empCNIC = new SimpleStringProperty(this,"empCNIC","");
	private StringProperty empPhone = new SimpleStringProperty(this,"empPhone","");
	private StringProperty empUserName = new SimpleStringProperty(this,"empUserName","");
	private StringProperty empPswd = new SimpleStringProperty(this,"empPswd","");
	private StringProperty empEmail = new SimpleStringProperty(this,"empEmail","");
	private ObjectProperty<Date> empDOB = new SimpleObjectProperty<Date>(this,"empDOB", new Date());
	private ObjectProperty<Date> empJoinDate = new SimpleObjectProperty<Date>(this,"empJoinDate", new Date());
	private BooleanProperty empActive = new SimpleBooleanProperty(this,"empActive",false);

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getEmpId() {
		return empId.get();
	}

	@NotNull
	public String getEmpName() {
		return empName.get();
	}

	@NotNull
	public String getEmpIdentity() {
		return empIdentity.get();
	}

	@NotNull
	public String getEmpAddress() {
		return empAddress.get();
	}

	@NotNull
	public String getEmpDesignation() {
		return empDesignation.get();
	}

	@NotNull
	@Column(unique = true)
	public String getEmpCNIC() {
		return empCNIC.get();
	}

	@NotNull
	public String getEmpPhone() {
		return empPhone.get();
	}
	
	public String getEmpEmail() {
		return empEmail.get();
	}

	@Column(unique = true)
	public String getEmpUserName() {
		return empUserName.get();
	}
	public String getEmpPswd() {
		return empPswd.get();
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	public Date getEmpDOB() {
		return empDOB.get();
	}
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date getEmpJoinDate() {
		return empJoinDate.get();
	}
	
	@NotNull
	public boolean getEmpActive() {
		return empActive.get();
	}

	//getProperty Methods
	public IntegerProperty empIdProperty() {
		return empId;
	}
	public StringProperty empNameProperty() {
		return empName;
	}
	public StringProperty empIdentityProperty() {
		return empIdentity;
	}
	public StringProperty empAddressProperty() {
		return empAddress;
	}
	public StringProperty empDesignationProperty() {
		return empDesignation;
	}
	public StringProperty empCNICProperty() {
		return empCNIC;
	}
	public StringProperty empPhoneProperty() {
		return empPhone;
	}
	public StringProperty empEmailProperty() {
		return empEmail;
	}
	public StringProperty empUserNameProperty() {
		return empUserName;
	}
	public StringProperty empPswdProperty() {
		return empPswd;
	}
	public ObjectProperty<Date> empDOBProperty(){
		return empDOB;
	}
	public ObjectProperty<Date> empJoinDateProperty(){
		return empJoinDate;
	}
	public BooleanProperty empActiveProperty() {
		return empActive;
	}


	//Setters

	public void setEmpId(int empId) {
		this.empId.set(empId);
	}
	public void setEmpName(String empName) {
		this.empName.set(empName);
	}
	public void setEmpIdentity(String empIdentity) {
		this.empIdentity.set(empIdentity);
	}
	public void setEmpAddress(String empAddress) {
		this.empAddress.set(empAddress);
	}
	public void setEmpDesignation(String empDesignation) {
		this.empDesignation.set(empDesignation);
	}
	public void setEmpCNIC(String empCNIC) {
		this.empCNIC.set(empCNIC);
	}
	public void setEmpPhone(String empPhone) {
		this.empPhone.set(empPhone);
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail.set(empEmail);
	}
	public void setEmpUserName(String empUserName) {
		this.empUserName.set(empUserName);
	}
	public void setEmpPswd(String empPswd) {
		this.empPswd.set(empPswd);
	}
	public void setEmpDOB(Date empDOB) {
		this.empDOB.set(empDOB);
	}
	public void setEmpJoinDate(Date empJoinDate) {
		this.empJoinDate.set(empJoinDate);
	}
	public void setEmpActive(boolean empActive) {
		this.empActive.set(empActive);
	}

	@Override
	public String toString() {
		return getEmpUserName();
	}

}
