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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Purchase {
	private IntegerProperty purchaseId = new SimpleIntegerProperty(this,"purchaseId",0);
	private ObjectProperty<Date> purchaseDate = new SimpleObjectProperty<Date>(this,"purchaseDate", new Date());
	private ObjectProperty<Supplier> purchaseSupplier = new SimpleObjectProperty<Supplier>(this,"purchaseSupplier",new Supplier());
	private ObjectProperty<Employee> purchaseEmployee = new SimpleObjectProperty<Employee>(this,"purchaseEmployee",new Employee());
	private StringProperty purchaseSupplierBill = new SimpleStringProperty(this,"purchaseSupplierBill","");
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getPurchaseId() {
		return purchaseId.get();
	}
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date getPurchaseDate() {
		return purchaseDate.get();
	}
	@ManyToOne
	@JoinColumn(name = "PurchaseSupplier")
	public Supplier getPurchaseSupplier() {
		return purchaseSupplier.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "PurchaseEmployee")
	public Employee getPurchaseEmployee() {
		return purchaseEmployee.get();
	}

	public String getPurchaseSupplierBill() {
		return purchaseSupplierBill.get();
	}
	

	//get Property

	public IntegerProperty purchaseIdProperty() {
		return purchaseId;
	}
	public ObjectProperty<Date> purchaseDateProperty() {
		return purchaseDate;
	}
	public ObjectProperty<Supplier> purchaseSupplierProperty() {
		return purchaseSupplier;
	}
	
	public ObjectProperty<Employee> purchaseEmployeeProperty() {
		return purchaseEmployee;
	}
	public StringProperty purchaseSupplierBillProperty() {
		return purchaseSupplierBill;
	}
	

	//setters
	public void setPurchaseId(int purchaseId) {
		this.purchaseId.set(purchaseId);
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate.set(purchaseDate);
	}
	public void setPurchaseSupplier(Supplier purchaseSupplier) {
		this.purchaseSupplier.set(purchaseSupplier);
	}
	public void setPurchaseEmployee(Employee purchaseEmployee) {
		this.purchaseEmployee.set(purchaseEmployee);
	}
	public void setPurchaseSupplierBill(String purchaseSupplierBill) {
		this.purchaseSupplierBill.set(purchaseSupplierBill);
	}
	
}
