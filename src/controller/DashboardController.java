package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.ReportsDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import model.Customer;
import model.Doctor;
import model.Drug;
import model.Employee;
import model.Supplier;
import javafx.scene.control.Label;

public class DashboardController implements Initializable{
	@FXML
	private HBox barColor;
	@FXML
	private Label lblCustomer;
	@FXML
	private Label lblSupplier;
	@FXML
	private Label lblEmployee;
	@FXML
	private Label lblProducts;
	@FXML
	private Label lblDoctor;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblCustomer.setText(String.valueOf(ReportsDB.rowCount(Customer.class, "custActive", true)));
		lblSupplier.setText(String.valueOf(ReportsDB.rowCount(Supplier.class, "suppActive", true)));
		lblEmployee.setText(String.valueOf(ReportsDB.rowCount(Employee.class, "empActive", true)));
		lblProducts.setText(String.valueOf(ReportsDB.rowCount(Drug.class, "drugActive", true)));
		lblDoctor.setText(String.valueOf(ReportsDB.rowCount(Doctor.class, "docActive", true)));
	}

}
