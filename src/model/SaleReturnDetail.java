package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import utils.Util;

@Entity
public class SaleReturnDetail {
	private IntegerProperty returnDetailId = new SimpleIntegerProperty(this,"returnDetailId",0);
	private ObjectProperty<SaleReturn> returnId = new SimpleObjectProperty<SaleReturn>(this,"returnId",new SaleReturn());
	private ObjectProperty<Stock> returnStockDrug = new SimpleObjectProperty<Stock>(this,"returnStockDrug",new Stock());
	private IntegerProperty returnQty = new SimpleIntegerProperty(this,"returnQty",0);
	private IntegerProperty saleQty = new SimpleIntegerProperty(this,"saleQty",0);
	private DoubleProperty tradePrice = new SimpleDoubleProperty(this,"tradePrice",0.0);
	private DoubleProperty retailPrice = new SimpleDoubleProperty(this,"retailPrice",0.0);
	private DoubleProperty totalPrice = new SimpleDoubleProperty(this,"totalPrice",0.0);
	private DoubleProperty trade = new SimpleDoubleProperty(this,"trade",0.0);
	
	public SaleReturnDetail() {
		totalPrice.bind(Bindings.multiply(returnQty, retailPrice));
		trade.bind(Bindings.multiply(returnQty, tradePrice));
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getReturnDetailId() {
		return returnDetailId.get();
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "ReturnId")
	public SaleReturn getReturnId() {
		return returnId.get();
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "returnStockDrug")
	public Stock getReturnStockDrug() {
		return returnStockDrug.get();
	}
	
	@NotNull
	public int getReturnQty() {
		return returnQty.get();
	}
	public int getSaleQty() {
		return saleQty.get();
	}
	
	@NotNull
	public double getTradePrice() {
		return Util.decimalTwoDigitsDouble(tradePrice.get());
	}
	
	@NotNull
	public double getRetailPrice() {
		return Util.decimalTwoDigitsDouble(retailPrice.get());
	}
	
	@Transient
	public double getTotalPrice() {
		return Util.decimalTwoDigitsDouble(totalPrice.get());
	}
	@Transient
	public double getTrade() {
		return Util.decimalTwoDigitsDouble(trade.get());
	}
	
	
	
	//getProperty
	public IntegerProperty returnDetailIdProperty() {
		return returnDetailId;
	}
	public ObjectProperty<SaleReturn> returnIdProperty() {
		return returnId;
	}
	public ObjectProperty<Stock> returnStockDrugProperty() {
		return returnStockDrug;
	}
	public IntegerProperty returnQtyProperty() {
		return returnQty;
	}
	public IntegerProperty saleQtyProperty() {
		return saleQty;
	}
	public DoubleProperty tradePriceProperty() {
		return tradePrice;
	}
	public DoubleProperty retailPriceProperty() {
		return retailPrice;
	}
	public DoubleProperty totalPriceProperty() {
		return totalPrice;
	}
	public DoubleProperty tradeProperty() {
		return trade;
	}
	
	//setters
	public void setReturnDetailId(int returnDetailId) {
		this.returnDetailId.set(returnDetailId);
		
	}
	public void setReturnId(SaleReturn returnId) {
		this.returnId.set(returnId);
	}
	public void setReturnStockDrug(Stock returnStockDrug) {
		this.returnStockDrug.set(returnStockDrug);
	}
	public void setReturnQty(int returnQty) {
		this.returnQty.set(returnQty);
	}
	public void setSaleQty(int saleQty) {
		this.saleQty.set(saleQty);
	}
	public void setTradePrice(double tradePrice) {
		this.tradePrice.set(Util.decimalTwoDigitsDouble(tradePrice));
	}
	public void setRetailPrice(double retailPrice) {
		this.retailPrice.set(Util.decimalTwoDigitsDouble(retailPrice));
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice.set(Util.decimalTwoDigitsDouble(totalPrice));
	}
	public void setTrade(double trade) {
		this.trade.set(Util.decimalTwoDigitsDouble(trade));
	}

}
