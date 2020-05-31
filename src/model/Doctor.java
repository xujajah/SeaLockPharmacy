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
public class Doctor {
	private IntegerProperty docId = new SimpleIntegerProperty(this, "docId",0);
	private StringProperty docName = new SimpleStringProperty(this,"docName","");
	private StringProperty docIdentity = new SimpleStringProperty(this,"docIdentity","");
	private StringProperty docAddress = new SimpleStringProperty(this,"docAddress","");
	private StringProperty docAffiliation = new SimpleStringProperty(this,"docAffiliation","");
	private StringProperty docEmail = new SimpleStringProperty(this,"docEmail","");
	private StringProperty docPhone = new SimpleStringProperty(this,"docPhone","");
	private ObjectProperty<Employee> docEmployee = new SimpleObjectProperty<Employee>(this,"docEmployee",new Employee());
	private BooleanProperty docActive = new SimpleBooleanProperty(this,"docActive",false);
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getDocId() {
		return docId.get();
	}
	
	@NotNull
	public String getDocName() {
		return docName.get();
	}
	
	@NotNull
	public String getDocIdentity() {
		return docIdentity.get();
	}
	
	public String getDocAddress() {
		return docAddress.get();
	}
	
	public String getDocAffiliation() {
		return docAffiliation.get();
	}
	
	public String getDocEmail() {
		return docEmail.get();
	}
	
	@NotNull
	public String getDocPhone() {
		return docPhone.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "docEmployee")
	public Employee getDocEmployee() {
		return docEmployee.get();
	}
	
	@NotNull
	public boolean getDocActive() {
		return docActive.get();
	}
	
	//getProperty Methods
	public IntegerProperty docIdProperty() {
		return docId;
	}
	public StringProperty docNameProperty() {
		return docName;
	}
	public StringProperty docIdentityProperty() {
		return docIdentity;
	}
	public StringProperty docAddressProperty() {
		return docAddress;
	}
	public StringProperty docAffiliationProperty() {
		return docAffiliation;
	}
	public StringProperty docEmailProperty() {
		return docEmail;
	}
	public StringProperty docPhoneProperty() {
		return docPhone;
	}
	public ObjectProperty<Employee> docEmployeeProperty(){
		return docEmployee;
	}
	public BooleanProperty docActiveProperty() {
		return docActive;
	}
	
	//Setters
	
	public void setDocId(int docId) {
		this.docId.set(docId);
	}
	public void setDocName(String docName) {
		this.docName.set(docName);
	}
	public void setDocIdentity(String docIdentity) {
		this.docIdentity.set(docIdentity);
	}
	public void setDocAddress(String docAddress) {
		this.docAddress.set(docAddress);
	}
	public void setDocAffiliation(String docAffiliation) {
		this.docAffiliation.set(docAffiliation);
	}
	public void setDocEmail(String docEmail) {
		this.docEmail.set(docEmail);
	}
	public void setDocPhone(String docPhone) {
		this.docPhone.set(docPhone);
	}
	public void setDocEmployee(Employee docEmployee) {
		this.docEmployee.set(docEmployee);
	}
	public void setDocActive(boolean docActive) {
		this.docActive.set(docActive);
	}
	
	@Override
	public String toString() {
		return getDocName();
	}
}
