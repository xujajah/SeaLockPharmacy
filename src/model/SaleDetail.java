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
public class SaleDetail {
	private IntegerProperty saleDetailId = new SimpleIntegerProperty(this,"saleDetailId",0);
	private ObjectProperty<Sale> saleId = new SimpleObjectProperty<Sale>(this,"saleId",new Sale());
	private ObjectProperty<Stock> saleStockDrug = new SimpleObjectProperty<Stock>(this,"saleStockDrug",new Stock());
	private IntegerProperty saleQty = new SimpleIntegerProperty(this,"saleQty",0);
	private DoubleProperty tradePrice = new SimpleDoubleProperty(this,"tradePrice",0.0);
	private DoubleProperty retailPrice = new SimpleDoubleProperty(this,"retailPrice",0.0);
	private DoubleProperty totalRetailPrice = new SimpleDoubleProperty(this,"totalPrice",0.0);
	private DoubleProperty totalTradePrice = new SimpleDoubleProperty(this,"totalPrice",0.0);
	
	public SaleDetail() {
		totalRetailPrice.bind(Bindings.multiply(saleQty, retailPrice));
		totalTradePrice.bind(Bindings.multiply(saleQty, tradePrice));
		
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getSaleDetailId() {
		return saleDetailId.get();
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "SaleId")
	public Sale getSaleId() {
		return saleId.get();
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "saleStockDrug")
	public Stock getSaleStockDrug() {
		return saleStockDrug.get();
	}
	
	@NotNull
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
	public double getTotalRetailPrice() {
		return Util.decimalTwoDigitsDouble(totalRetailPrice.get());
	}
	@Transient
	public double getTotalTradePrice() {
		return Util.decimalTwoDigitsDouble(totalTradePrice.get());
	}
	
	
	
	//getProperty
	public IntegerProperty saleDetailIdProperty() {
		return saleDetailId;
	}
	public ObjectProperty<Sale> saleIdProperty() {
		return saleId;
	}
	public ObjectProperty<Stock> saleStockDrugProperty() {
		return saleStockDrug;
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
	public DoubleProperty totalRetailPriceProperty() {
		return totalRetailPrice;
	}
	public DoubleProperty totalTradePriceProperty() {
		return totalTradePrice;
	}
	
	//setters
	public void setSaleDetailId(int saleDetailId) {
		this.saleDetailId.set(saleDetailId);
		
	}
	public void setSaleId(Sale saleId) {
		this.saleId.set(saleId);
	}
	public void setSaleStockDrug(Stock saleStockDrug) {
		this.saleStockDrug.set(saleStockDrug);
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
	public void setTotalRetailPrice(double totalRetailPrice) {
		this.totalRetailPrice.set(Util.decimalTwoDigitsDouble(totalRetailPrice));
	}
	public void setTotalTradePrice(double totalTradePrice) {
		this.totalTradePrice.set(Util.decimalTwoDigitsDouble(totalTradePrice));
	}
	
	
	

}
