package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.DataBase;
import database.PrescriptionDB;
import database.PrescriptionDetailDB;
import database.StockDB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Customer;
import model.Doctor;
import model.Stock;

public class SaleController implements Initializable{
	@FXML
	private BorderPane rootPurchaiseItem;
	@FXML
	private HBox barColor;
	@FXML
	private TabSaleController tabSaleController;
	
	@FXML
	private TabReturnController tabReturns;

	@FXML
	private TabPane tabPane;

	private StockDB dbStock;
	private DataBase dbCustomer;
	private DataBase dbDoctor;
	private PrescriptionDB dbPrescription;
	private PrescriptionDetailDB dbPrescriptionDetail;
	private ObservableList<Stock> stockList;
	private ObservableList<Customer> customersList;
	private ObservableList<Doctor> doctorsList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dbStock = new StockDB();
		dbCustomer = new DataBase();
		dbDoctor = new DataBase();
		dbPrescription = new PrescriptionDB();
		dbPrescriptionDetail = new PrescriptionDetailDB();

		stockList = dbStock.stockReterive();
		customersList = dbCustomer.retrieveAscOrder(Customer.class, "custActive", true, "custName");
		doctorsList = dbDoctor.retrieveAscOrder(Doctor.class, "docActive", true, "docName");

		tabSaleController.setData(dbStock,stockList,dbCustomer,customersList,dbDoctor,doctorsList,dbPrescription,dbPrescriptionDetail);
	}

}
