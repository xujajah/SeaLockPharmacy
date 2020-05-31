package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import utils.Util;

public class ReportTopSellingDrug {
	private ObjectProperty<Stock> stockDrug = new SimpleObjectProperty<Stock>(this,"stockDrug", new Stock());
	private IntegerProperty totalOrders = new SimpleIntegerProperty(this,"totalOrders",0);
	private IntegerProperty totalQty = new SimpleIntegerProperty(this,"totalQty",0);
//	private DoubleProperty tradePrice = new SimpleDoubleProperty(this,"tradePrice",0.0);
//	private DoubleProperty retailPrice = new SimpleDoubleProperty(this,"retailPrice",0.0);
	private DoubleProperty totalRetailPrice = new SimpleDoubleProperty(this,"totalRetailPrice",0.0);
	private DoubleProperty totalTradePrice = new SimpleDoubleProperty(this,"totalTradePrice",0.0);
	private DoubleProperty totalProfit = new SimpleDoubleProperty(this,"totalProfit",0.0);
	
	public Stock getStockDrug() {
		return stockDrug.get();
	}
	public int getTotalOrders() {
		return totalOrders.get();
	}
	public int getTotalQty() {
		return totalQty.get();
	}
//	public double getTradePrice() {
//		return Util.decimalTwoDigitsDouble(tradePrice.get());
//	}
//	public double getRetailPrice() {
//		return Util.decimalTwoDigitsDouble(retailPrice.get());
//	}
	public double getTotalRetailPrice() {
		return Util.decimalTwoDigitsDouble(totalRetailPrice.get());
	}
	public double getTotalTradePrice() {
		return Util.decimalTwoDigitsDouble(totalTradePrice.get());
	}
	public double getTotalProfit() {
		return Util.decimalTwoDigitsDouble(totalProfit.get());
	}
	
	
	//property
	public ObjectProperty<Stock> stockDrugProperty() {
		return stockDrug;
	}
	public IntegerProperty totalOrdersProperty() {
		return totalOrders;
	}
	public IntegerProperty totalQtyProperty() {
		return totalQty;
	}
//	public DoubleProperty tradePriceProperty() {
//		return tradePrice;
//	}
//	public DoubleProperty retailPriceProperty() {
//		return retailPrice;
//	}
	public DoubleProperty totalRetailPriceProperty() {
		return totalRetailPrice;
	}
	public DoubleProperty totalTradePriceProperty() {
		return totalTradePrice;
	}
	public DoubleProperty totalProfitProperty() {
		return totalProfit;
	}
	
	
	//setters  
	
	
	public void setStockDrug(Stock stockDrug) {
		this.stockDrug.set(stockDrug);
	}
	public void setTotalOrders(int totalOrders) {
		this.totalOrders.set(totalOrders);
	}
	public void setTotalQty(int totalQty) {
		this.totalQty.set(totalQty);
	}
//	public void setTradePrice(double tradePrice) {
//		this.tradePrice.set(Util.decimalTwoDigitsDouble(tradePrice));
//	}
//	public void setRetailPrice(double retailPrice) {
//		this.retailPrice.set(Util.decimalTwoDigitsDouble(retailPrice));
//	}
	public void setTotalRetailPrice(double totalRetailPrice) {
		this.totalRetailPrice.set(Util.decimalTwoDigitsDouble(totalRetailPrice));
	}
	public void setTotalTradePrice(double totalTradePrice) {
		this.totalTradePrice.set(Util.decimalTwoDigitsDouble(totalTradePrice));
	}
	public void setTotalProfit(double totalProfit) {
		this.totalProfit.set(Util.decimalTwoDigitsDouble(totalProfit));
	}
	
	

}
