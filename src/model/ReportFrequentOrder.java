package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportFrequentOrder {
	
	private ObjectProperty<Customer> customer = new SimpleObjectProperty<Customer>(this,"customer", new Customer());
//	private ObjectProperty<Sale> sale = new SimpleObjectProperty<Sale>(this,"sale", new Sale());
	private StringProperty totalOrders = new SimpleStringProperty(this,"totalOrders","");
	
	
	
	public Customer getCustomer() {
		return customer.get();
	}
//	public Sale getSale() {
//		return sale.get();
//	}
	public String getTotalOrders() {
		return totalOrders.get();
	}
	
	
	
	public ObjectProperty<Customer> customerProperty() {
		return customer;
	}
//	public ObjectProperty<Sale> saleProperty() {
//		return sale;
//	}
	public StringProperty totalOrdersProperty() {
		return totalOrders;
	}
	
	
	
	public void setCustomer(Customer customer) {
		this.customer.set(customer);
	}
//	public void setSale(Sale sale) {
//		this.sale.set(sale);
//	}
	
	public void setTotalOrders(String totalOrders) {
		this.totalOrders.set(totalOrders);
	}
	

}
