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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import utils.Util;

@Entity
public class Sale {
	private IntegerProperty saleId = new SimpleIntegerProperty(this,"saleId",0);
	private ObjectProperty<Date> saleDate = new SimpleObjectProperty<Date>(this,"saleDate",new Date());
	private ObjectProperty<Date> saleTime = new SimpleObjectProperty<Date>(this,"saleTime",new Date());
	private ObjectProperty<Customer> saleCustomer = new SimpleObjectProperty<Customer>(this,"saleCustomer", new Customer());
	private ObjectProperty<Doctor> saleDoctor = new SimpleObjectProperty<Doctor>(this,"saleDoctor", new Doctor());
	private ObjectProperty<Prescription> salePrescription = new SimpleObjectProperty<Prescription>(this,"salePrescripion",new Prescription());
	private ObjectProperty<Employee> saleEmployee = new SimpleObjectProperty<Employee>(this,"saleEmployee",new Employee());
	private BooleanProperty saleActive = new SimpleBooleanProperty(this,"saleActive",true);
	private DoubleProperty totalTradePrice = new SimpleDoubleProperty(this,"totalTradePrice",0.0);
	private DoubleProperty totalRetailPrice = new SimpleDoubleProperty(this,"totalRetailPrice",0.0);
	private DoubleProperty totalDiscount = new SimpleDoubleProperty(this,"totalDiscount",0.0);
	private DoubleProperty totalPayable = new SimpleDoubleProperty(this,"totalPayable",0.0);
	private DoubleProperty totalProfit = new SimpleDoubleProperty(this,"totalProfit",0.0);
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getSaleId() {
		return saleId.get();
	}
	@NotNull
	@Temporal (TemporalType.DATE)
	public Date getSaleDate() {
		return saleDate.get();
	}
	@NotNull
	@Temporal (TemporalType.TIME)
	public Date getSaleTime() {
		return saleTime.get();
	}
	
	@ManyToOne
	@JoinColumn (name = "saleCustomer")
	public Customer getSaleCustomer() {
		return saleCustomer.get();
	}
	@ManyToOne
	@JoinColumn (name = "saleDoctor")
	public Doctor getSaleDoctor() {
		return saleDoctor.get();
	}
	@ManyToOne
	@JoinColumn (name = "salePrescription")
	public Prescription getSalePrescription() {
		return salePrescription.get();
	}
	@ManyToOne
	@JoinColumn (name = "saleEmployee")
	public Employee getSaleEmployee() {
		return saleEmployee.get();
	}
	@NotNull
	public boolean getSaleActive() {
		return saleActive.get();
	}
	
	public double getTotalTradePrice() {
		return Util.decimalTwoDigitsDouble(totalTradePrice.get());
	}
	public double getTotalRetailPrice() {
		return Util.decimalTwoDigitsDouble(totalRetailPrice.get());
	}
	public double getTotalDiscount() {
		return Util.decimalTwoDigitsDouble(totalDiscount.get());
	}
	public double getTotalPayable() {
		return Util.decimalTwoDigitsDouble(totalPayable.get());
	}
	public double getTotalProfit() {
		return Util.decimalTwoDigitsDouble(totalProfit.get());
	}
	//get Property
	public IntegerProperty saleIdProperty() {
		return saleId;
	}
	public ObjectProperty<Date> saleDateProperty() {
		return saleDate;
	}
	public ObjectProperty<Date> saleTimeProperty() {
		return saleTime;
	}
	public ObjectProperty<Customer> saleCustomerProperty() {
		return saleCustomer;
	}
	public ObjectProperty<Doctor> saleDoctorProperty() {
		return saleDoctor;
	}
	public ObjectProperty<Prescription> salePrescriptionProperty() {
		return salePrescription;
	}
	public ObjectProperty<Employee> saleEmployeeProperty() {
		return saleEmployee;
	}
	public boolean saleActiveProperty() {
		return saleActive.get();
	}
	public DoubleProperty totalTradePriceProperty() {
		return totalTradePrice;
	}
	public DoubleProperty totalRetailPriceProperty() {
		return totalRetailPrice;
	}
	public DoubleProperty totalDiscountProperty() {
		return totalDiscount;
	}
	public DoubleProperty totalPayableProperty() {
		return totalPayable;
	}
	public DoubleProperty totalProfitProperty() {
		return totalProfit;
	}


	//setters
	public void setSaleId(int saleId) {
		this.saleId.set(saleId);
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate.set(saleDate);
	}
	public void setSaleTime(Date saleTime) {
		this.saleTime.set(saleTime);
	}
	public void setSaleCustomer(Customer saleCustomer) {
		this.saleCustomer.set(saleCustomer);
	}
	public void setSaleDoctor(Doctor saleDoctor) {
		this.saleDoctor.set(saleDoctor);
	}
	public void setSalePrescription(Prescription salePrescription) {
		this.salePrescription.set(salePrescription);
	}
	public void setSaleEmployee(Employee saleEmployee) {
		this.saleEmployee.set(saleEmployee);
	}
	public void setSaleActive(boolean saleActive) {
		this.saleActive.set(saleActive);
	}
	public void setTotalTradePrice(double totalTradePrice) {
		this.totalTradePrice.set(Util.decimalTwoDigitsDouble(totalTradePrice));
	}
	public void setTotalRetailPrice(double totalRetailPrice) {
		this.totalRetailPrice.set(Util.decimalTwoDigitsDouble(totalRetailPrice));
	}
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount.set(Util.decimalTwoDigitsDouble(totalDiscount));
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable.set(Util.decimalTwoDigitsDouble(totalPayable));
	}
	public void setTotalProfit(double totalProfit) {
		this.totalProfit.set(Util.decimalTwoDigitsDouble(totalProfit));
	}
	

}
