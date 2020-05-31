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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import utils.Util;

@Entity
public class SaleReturn {
	private IntegerProperty returnId = new SimpleIntegerProperty(this,"returnId",0);
	private ObjectProperty<Date> returnDate = new SimpleObjectProperty<Date>(this,"returnDate",new Date());
	private ObjectProperty<Date> returnTime = new SimpleObjectProperty<Date>(this,"returnTime",new Date());
	private ObjectProperty<Sale> saleReturnId = new SimpleObjectProperty<Sale>(this,"saleReturnId", new Sale());
	private DoubleProperty totalTradePrice = new SimpleDoubleProperty(this,"totalTradePrice",0.0);
	private DoubleProperty totalRetailPrice = new SimpleDoubleProperty(this,"totalRetailPrice",0.0);
	private DoubleProperty totalReturnProfit = new SimpleDoubleProperty(this,"totalReturnProfit",0.0);
	private ObjectProperty<Employee> returnEmployee = new SimpleObjectProperty<Employee>(this,"returnEmployee",new Employee());
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getReturnId() {
		return returnId.get();
	}
	@NotNull
	@Temporal (TemporalType.DATE)
	public Date getReturnDate() {
		return returnDate.get();
	}
	@NotNull
	@Temporal (TemporalType.TIME)
	public Date getReturnTime() {
		return returnTime.get();
	}
	
	@ManyToOne
	@JoinColumn (name = "saleReturnId")
	public Sale getSaleReturnId() {
		return saleReturnId.get();
	}
	
	@ManyToOne
	@JoinColumn (name = "returnEmployee")
	public Employee getReturnEmployee() {
		return returnEmployee.get();
	}
	
	public double getTotalTradePrice() {
		return Util.decimalTwoDigitsDouble(totalTradePrice.get());
	}
	public double getTotalRetailPrice() {
		return Util.decimalTwoDigitsDouble(totalRetailPrice.get());
	}
	public double getTotalReturnProfit() {
		return Util.decimalTwoDigitsDouble(totalReturnProfit.get());
	}
	
	
	
	//get Property
	public IntegerProperty returnIdProperty() {
		return returnId;
	}
	public ObjectProperty<Date> returnDateProperty() {
		return returnDate;
	}
	public ObjectProperty<Date> returnTimeProperty() {
		return returnTime;
	}
	public ObjectProperty<Sale> saleReturnIdProperty() {
		return saleReturnId;
	}
	public DoubleProperty totalTradePriceProperty() {
		return totalTradePrice;
	}
	public DoubleProperty totalRetailPriceProperty() {
		return totalRetailPrice;
	}
	public DoubleProperty totalReturnProfitProperty() {
		return totalReturnProfit;
	}
	public ObjectProperty<Employee> returnEmployeeProperty() {
		return returnEmployee;
	}
	

	//setters
	public void setReturnId(int returnId) {
		this.returnId.set(returnId);
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate.set(returnDate);
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime.set(returnTime);
	}
	public void setSaleReturnId(Sale saleReturnId) {
		this.saleReturnId.set(saleReturnId);
	}
	public void setTotalTradePrice(double totalTradePrice) {
		this.totalTradePrice.set(Util.decimalTwoDigitsDouble(totalTradePrice));
	}
	public void setTotalRetailPrice(double totalRetailPrice) {
		this.totalRetailPrice.set(Util.decimalTwoDigitsDouble(totalRetailPrice));
	}
	public void setTotalReturnProfit(double totalReturnProfit) {
		this.totalReturnProfit.set(Util.decimalTwoDigitsDouble(totalReturnProfit));
	}
	public void setReturnEmployee(Employee returnEmployee) {
		this.returnEmployee.set(returnEmployee);
	}
}
