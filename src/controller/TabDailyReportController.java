package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

import database.ReportsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Customer;
import model.Sale;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabDailyReportController implements Initializable{

	@FXML
	private BorderPane rootDailyReport;

	@FXML
	private JFXDatePicker dateDR;

	@FXML
	private TableView<Sale> tabDR;

	@FXML
	private TableColumn<Sale, Number> colSerial;

	@FXML
	private TableColumn<Sale, Number> colID;

	@FXML
	private TableColumn<Sale, Customer> colCustomerName;

	@FXML
	private TableColumn<Sale, Date> colDate;

	@FXML
	private TableColumn<Sale, Number> colTotalAmount;

	@FXML
	private TableColumn<Sale, Number> colProfit;

	@FXML
	private Label lblTotalSale;

	@FXML
	private Label lblTotalProfit;


	private ObservableList<Sale> sales = FXCollections.observableArrayList();
	private double totalSale = 0;
	private double totalProfit = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Util.setDatePicker(dateDR);
		dateDR.setValue(LocalDate.now());
		dateDR.setEditable(false);
		setTableCell();
		sales = ReportsDB.dailySale(Util.localDateToJavaSqlDate(LocalDate.now()));
		tabDR.setItems(sales);
		for(Sale sale : sales) {
			totalSale = totalSale + sale.getTotalPayable();
			totalProfit = totalProfit + sale.getTotalProfit();
		}
		lblTotalProfit.setText(Util.decimalTwoDigitsString(totalProfit));
		lblTotalSale.setText(Util.decimalTwoDigitsString(totalSale));
		onSelectDate();
	}
	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<Sale,Number>, TableCell<Sale,Number>>() {

			@Override
			public TableCell<Sale, Number> call(TableColumn<Sale, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colID.setCellValueFactory(data -> data.getValue().saleIdProperty());
		colCustomerName.setCellValueFactory(data -> data.getValue().saleCustomerProperty());
		colDate.setCellValueFactory(data -> data.getValue().saleDateProperty());
		colTotalAmount.setCellValueFactory(data -> data.getValue().totalPayableProperty());
		colProfit.setCellValueFactory(data -> data.getValue().totalProfitProperty());
		
		colTotalAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
		colProfit.setStyle("-fx-alignment: CENTER-RIGHT;");
	}

	private void onSelectDate() {
		dateDR.setOnAction(event ->{
			if(dateDR.getValue()==null) {
				Dialog.error("Please Select 'Date' First");
			}
			else {
				sales.clear();
				totalProfit = 0;
				totalSale = 0;
				sales = ReportsDB.dailySale(Util.localDateToJavaSqlDate(dateDR.getValue()));
				if(!sales.isEmpty()) {
					tabDR.setItems(sales);
					for(Sale sale : sales) {
						totalSale = totalSale + sale.getTotalPayable();
						totalProfit = totalProfit + sale.getTotalProfit();
					}
					lblTotalProfit.setText(Util.decimalTwoDigitsString(totalProfit));
					lblTotalSale.setText(Util.decimalTwoDigitsString(totalSale));
				}else {
					lblTotalProfit.setText(Util.decimalTwoDigitsString(totalProfit));
					lblTotalSale.setText(Util.decimalTwoDigitsString(totalSale));
					Dialog.information("No Record Found on Selected Date" );
				}
			}
		});
	}

}