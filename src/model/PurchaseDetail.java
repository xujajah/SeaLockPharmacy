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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.Util;

@Entity
public class PurchaseDetail {
	private IntegerProperty purchaseDetailId = new SimpleIntegerProperty(this,"purchaseDetailId",0);
	private IntegerProperty qtyPerUnit = new SimpleIntegerProperty(this,"qtyPerUnit",0);
	private DoubleProperty tradePrice = new SimpleDoubleProperty(this,"trdaePrice",0.0);
	private DoubleProperty retailPrice = new SimpleDoubleProperty(this,"retailPrice",0.0);
	private StringProperty batchNo = new SimpleStringProperty(this,"batchNo","");
	private ObjectProperty<Date> expiryDate = new SimpleObjectProperty<Date>(this,"expiryDate", new Date());
	private ObjectProperty<Drug> purchaseDrug = new SimpleObjectProperty<Drug>(this,"purchaseDrug",new Drug());
	private BooleanProperty expiryActive = new SimpleBooleanProperty(this,"expiryActive",false);
	private ObjectProperty<Purchase> purchase = new SimpleObjectProperty<Purchase>(this,"purchase",new Purchase());
	

	

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getPurchaseDetailId() {
		return purchaseDetailId.get();
	}
	
	@NotNull
	public int getQtyPerUnit() {
		return qtyPerUnit.get();
	}
	@NotNull
	public double getTradePrice() {
		return Util.decimalTwoDigitsDouble(tradePrice.get());
	}
	@NotNull
	public double getRetailPrice() {
		return Util.decimalTwoDigitsDouble(retailPrice.get());
	}
	public String getBatchNo() {
		return batchNo.get();
	}
	@Temporal (TemporalType.DATE)
	public Date getExpiryDate() {
		return expiryDate.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "PurchaseDrug")
	public Drug getPurchaseDrug() {
		return purchaseDrug.get();
	}
	@NotNull
	public boolean getExpiryActive() {
		return expiryActive.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "purchase")
	public Purchase getPurchase() {
		return purchase.get();
	}
	
	//get Property
	public IntegerProperty purchaseDetailIdProperty() {
		return purchaseDetailId;
	}
	public IntegerProperty qtyPerUnitProperty() {
		return qtyPerUnit;
	}
	public DoubleProperty tradePriceProperty() {
		return tradePrice;
	}
	public DoubleProperty retailPriceProperty() {
		return retailPrice;
	}
	public StringProperty batchNoProperty() {
		return batchNo;
	}
	public ObjectProperty<Date> expiryDateProperty() {
		return expiryDate;
	}
	public ObjectProperty<Drug> purchaseDrugProperty() {
		return purchaseDrug;
	}
	public BooleanProperty expiryActiveProperty() {
		return expiryActive;
	}
	public ObjectProperty<Purchase> purchaseProperty() {
		return purchase;
	}
	
	//setters
	public void setPurchaseDetailId(int purchaseDetailId) {
		this.purchaseDetailId.set(purchaseDetailId);
	}
	public void setQtyPerUnit(int qtyPerUnit) {
		this.qtyPerUnit.set(qtyPerUnit);
	}
	public void setTradePrice(double tradePrice) {
		this.tradePrice.set(Util.decimalTwoDigitsDouble(tradePrice));
	}
	public void setRetailPrice(double retailPrice) {
		this.retailPrice.set(Util.decimalTwoDigitsDouble(retailPrice));
	}
	public void setBatchNo(String batchNo) {
		this.batchNo.set(batchNo);
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate.set(expiryDate);
	}
	public void setPurchaseDrug(Drug purchaseDrug) {
		this.purchaseDrug.set(purchaseDrug);
	}
	public void setExpiryActive(boolean expiryActive) {
		this.expiryActive.set(expiryActive);
	}
	public void setPurchase(Purchase purchase) {
		this.purchase.set(purchase);
	}

}
