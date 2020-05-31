package model;

import javax.persistence.Column;
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
public class Drug {
	private IntegerProperty drugId = new SimpleIntegerProperty(this,"drugId",0);
	private StringProperty drugCommonName = new SimpleStringProperty(this,"drugCommonName","");
	private StringProperty drugMedicalName = new SimpleStringProperty(this,"drugMedicalName","");
	private StringProperty drugCategory = new SimpleStringProperty(this,"drugCategory","");
	private StringProperty drugDosage = new SimpleStringProperty(this,"drugDosage","");
	private StringProperty drugDescription = new SimpleStringProperty(this,"drugDescription","");
	private StringProperty drugUPC = new SimpleStringProperty(this,"drugUPC","");
	private StringProperty drugControlDrug = new SimpleStringProperty(this,"drugControlDrug","");
	private StringProperty drugManufacturer = new SimpleStringProperty(this,"drugManufacturer","");
	private IntegerProperty drugQtyPerPack = new SimpleIntegerProperty(this,"drugQtyPerPack",0);
	private ObjectProperty<Employee> drugEmployee = new SimpleObjectProperty<Employee>(this,"drugEmployee",new Employee());
	private BooleanProperty drugActive = new SimpleBooleanProperty(this,"drugActive",false);
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getDrugId() {
		return drugId.get();
	}
	
	@NotNull
	@Column (unique = true)
	public String getDrugCommonName() {
		return drugCommonName.get();
	}
	
	public String getDrugMedicalName() {
		return drugMedicalName.get();
	}
	
	public String getDrugCategory() {
		return drugCategory.get();
	}
	public String getDrugDosage() {
		return drugDosage.get();
	}
	public String getDrugDescription() {
		return drugDescription.get();
	}
	
	@NotNull
	@Column (unique = true)
	public String getDrugUPC() {
		return drugUPC.get();
	}
	
	public String getDrugControlDrug() {
		return drugControlDrug.get();
	}
	
	public String getDrugManufacturer() {
		return drugManufacturer.get();
	}
	
	@NotNull
	public Integer getDrugQtyPerPack() {
		return drugQtyPerPack.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "drugEmployee")
	public Employee getDrugEmployee() {
		return drugEmployee.get();
	}
	
	@NotNull
	public boolean getDrugActive() {
		return drugActive.get();
	}
	
	//getProperty Methods
	public IntegerProperty drugIdProperty() {
		return drugId;
	}
	public StringProperty drugCommonNameProperty() {
		return drugCommonName;
	}
	public StringProperty drugMedicalNameProperty() {
		return drugMedicalName;
	}
	public StringProperty drugCategoryProperty() {
		return drugCategory;
	}
	public StringProperty drugDosageProperty() {
		return drugDosage;
	}
	public StringProperty drugDescriptionProperty() {
		return drugDescription;
	}
	public StringProperty drugUPCProperty() {
		return drugUPC;
	}
	public StringProperty drugControlDrugProperty() {
		return drugControlDrug;
	}
	public StringProperty drugManufacturerProperty() {
		return drugManufacturer;
	}
	public IntegerProperty drugQtyPerPackProperty() {
		return drugQtyPerPack;
	}
	public ObjectProperty<Employee> drugEmployeeProperty(){
		return drugEmployee;
	}
	public BooleanProperty drugActiveProperty() {
		return drugActive;
	}
	
	//setters
	public void setDrugId(int drugId) {
		this.drugId.set(drugId);
	}
	public void setDrugCommonName(String drugCommonName) {
		this.drugCommonName.set(drugCommonName);
	}
	public void setDrugMedicalName(String drugMedicalName) {
		this.drugMedicalName.set(drugMedicalName);
	}
	public void setDrugCategory(String drugCategory) {
		this.drugCategory.set(drugCategory);
	}
	public void setDrugDosage(String drugDosage) {
		this.drugDosage.set(drugDosage);
	}
	public void setDrugDescription(String drugDescription) {
		this.drugDescription.set(drugDescription);
	}
	public void setDrugUPC(String drugUPC) {
		this.drugUPC.set(drugUPC);
	}
	public void setDrugControlDrug(String drugControlDrug) {
		this.drugControlDrug.set(drugControlDrug);
	}
	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer.set(drugManufacturer);
	}
	public void setDrugQtyPerPack(int drugQtyPerPack) {
		this.drugQtyPerPack.set(drugQtyPerPack);
	}
	public void setDrugEmployee(Employee drugEmployee) {
		this.drugEmployee.set(drugEmployee);
	}
	public void setDrugActive(boolean drugActive) {
		this.drugActive.set(drugActive);
	}
	
	
	@Override
	public String toString() {
		return getDrugCommonName();
	}
	

}
