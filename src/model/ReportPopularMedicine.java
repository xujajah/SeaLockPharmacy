package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ReportPopularMedicine {
	private ObjectProperty<Stock> stockDrug = new SimpleObjectProperty<Stock>(this,"stockDrug", new Stock());
	
	public Stock getStockDrug() {
		return stockDrug.get();
	}

}