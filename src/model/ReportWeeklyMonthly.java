package model;

import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ReportWeeklyMonthly {
	private ObjectProperty<Date> date = new SimpleObjectProperty<Date>(this,"date",new Date());
	private DoubleProperty totalPayable = new SimpleDoubleProperty(this,"totalPayable",0.0);
	private DoubleProperty totalProfit = new SimpleDoubleProperty(this,"totalProfit",0.0);
	
	public Date getDate() {
		return date.get();
	}
	public double getTotalPayable() {
		return totalPayable.get();
	}
	public double getTotalProfit() {
		return totalProfit.get();
	}
	
	//property
	public ObjectProperty<Date> dateProperty() {
		return date;
	}
	public DoubleProperty totalPayableProperty() {
		return totalPayable;
	}
	public DoubleProperty totalProfitProperty() {
		return totalProfit;
	}
	
	//setters
	public void setDate(Date date) {
		this.date.set(date);
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable.set(totalPayable);
	}
	public void setTotalProfit(double totalProfit) {
		this.totalProfit.set(totalProfit);
	}

}
