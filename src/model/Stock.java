package model;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import utils.Util;

@Entity
public class Stock {
	private IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
	private ObjectProperty<Drug> stockDrug = new SimpleObjectProperty<Drug>(this,"stockDrug",new Drug());
	private IntegerProperty stockTotal = new SimpleIntegerProperty(this,"stockTotal",0);
	private DoubleProperty stockTradePrice = new SimpleDoubleProperty(this,"trdaePrice",0.0);
	private DoubleProperty stockRetailPrice = new SimpleDoubleProperty(this,"retailPrice",0.0);
	private DoubleProperty stockUnitPrice = new SimpleDoubleProperty(this,"stockUnitPrice",0.0);
	
//	public Stock() {
//		stockUnitPrice.set(stockRetailPrice.get() / stockDrug.get().getDrugQtyPerPack());
//	}
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getId() {
		return id.get();
	}
	
	@OneToOne
	@JoinColumn (name = "drugId")
	public Drug getStockDrug() {
		return stockDrug.get();
	}
	
	@NotNull
	public Integer getStockTotal() {
		return stockTotal.get();
	}
	
	@NotNull
	public double getStockTradePrice() {
		return Util.decimalTwoDigitsDouble(stockTradePrice.get());
	}
	
	@NotNull
	public double getStockRetailPrice() {
		return Util.decimalTwoDigitsDouble(stockRetailPrice.get());
	}
	
	@Transient
	public double getStockUnitPrice() {
		return stockUnitPrice.get();
	}
	
	
	//get property
	
	public IntegerProperty idProperty() {
		return id;
	}
	public ObjectProperty<Drug> stockDrugProperty() {
		return stockDrug;
	}
	public IntegerProperty stockTotalProperty() {
		return stockTotal;
	}
	public DoubleProperty stockTradePriceProperty() {
		return stockTradePrice;
	}
	public DoubleProperty stockRetailPriceProperty() {
		return stockRetailPrice;
	}
	public DoubleProperty stockUnitPriceProperty() {
		setStockUnitPrice(Util.decimalTwoDigitsDouble(stockRetailPrice.get()/stockDrug.get().getDrugQtyPerPack()));
		return stockUnitPrice;
	}
	
	//setters
	public void setId(int id) {
		this.id.set(id);
	}
	public void setStockDrug(Drug stockDrug) {
		this.stockDrug.set(stockDrug);
	}
	public void setStockTotal(int stockTotal) {
		this.stockTotal.set(stockTotal);
	}
	public void setStockTradePrice(double stockTradePrice) {
		this.stockTradePrice.set(Util.decimalTwoDigitsDouble(stockTradePrice));
	}
	public void setStockRetailPrice(double stockRetailPrice) {
		this.stockRetailPrice.set(Util.decimalTwoDigitsDouble(stockRetailPrice));
	}

	public void setStockUnitPrice(double stockUnitPrice) {
		this.stockUnitPrice.set(Util.decimalTwoDigitsDouble(stockUnitPrice));
	}
	
	@Override
	public String toString() {
		return getStockDrug().toString();
	}

}
