package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

import database.ReportsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.Sale;
import model.SaleReturn;
import utils.Dialog;
import utils.Util;

public class TabRevenueController implements Initializable{

	@FXML
	private BorderPane root;

	@FXML
	private JFXDatePicker dateREVfrom;

	@FXML
	private JFXDatePicker dateREVto;

	@FXML
	private Label lblTotalAmount;

	@FXML
	private Label lblTotalProfit;

	@FXML
	private TableView<Sale> tabREVOrder;

	@FXML
	private TableColumn<Sale, Number> colOrderId;

	@FXML
	private TableColumn<Sale, Number> colOrderAmount;

	@FXML
	private TableColumn<Sale, Number> colOrderProfit;

	@FXML
	private Label lblTotalOrderAmount;

	@FXML
	private Label lblTotalOrderProfit;

	@FXML
	private TableView<SaleReturn> tabREVReturn;

	@FXML
	private TableColumn<SaleReturn, Number> colReturnId;

	@FXML
	private TableColumn<SaleReturn, Number> colReturnAmount;

	@FXML
	private TableColumn<SaleReturn, Number> colReturnProfit;

	@FXML
	private Label lblTotalReturnAmount;

	@FXML
	private Label lblTotalReturnProfit;
	private ObservableList<Sale> orders = FXCollections.observableArrayList();
	private ObservableList<SaleReturn> orderReturns = FXCollections.observableArrayList();
	private double totalOrderAmount = 0;
	private double totalOrderProfit = 0;

	private double totalReturnAmount = 0;
	private double totalReturnProfit = 0;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.setDatePicker(dateREVfrom);
		dateREVfrom.setEditable(false);
		dateREVto.setEditable(false);
		onDateFrom();
		onDateTo();
		setTableCell();
	}

	private void setTableCell() {
		colOrderId.setCellValueFactory(data -> data.getValue().saleIdProperty());
		colOrderAmount.setCellValueFactory(data -> data.getValue().totalPayableProperty());
		colOrderProfit.setCellValueFactory(data -> data.getValue().totalProfitProperty());
		colOrderAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
		colOrderProfit.setStyle("-fx-alignment: CENTER-RIGHT;");
		

		colReturnId.setCellValueFactory(data -> data.getValue().returnIdProperty());
		colReturnAmount.setCellValueFactory(data -> data.getValue().totalRetailPriceProperty());
		colReturnProfit.setCellValueFactory(data -> data.getValue().totalReturnProfitProperty());
		colReturnAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
		colReturnProfit.setStyle("-fx-alignment: CENTER-RIGHT;");
	}

	private void onDateFrom() {
		dateREVfrom.setOnAction(event ->{
			Util.setDatePicker(dateREVfrom, dateREVto);
			dateREVto.requestFocus();
		});
	}

	private void onDateTo() {
		dateREVto.setOnAction(event -> {
			if(dateREVfrom.getValue()== null) {
				Dialog.error("Please Select 'From Date' First");
			}
			else {
				orders.clear();
				orderReturns.clear();
				totalOrderAmount = 0;
				totalOrderProfit = 0;

				totalReturnAmount = 0;
				totalReturnProfit = 0;
				orders = ReportsDB.totalSale(Util.localDateToJavaSqlDate(dateREVfrom.getValue()) , Util.localDateToJavaSqlDate(dateREVto.getValue()));
				orderReturns = ReportsDB.totalReturn(Util.localDateToJavaSqlDate(dateREVfrom.getValue()) , Util.localDateToJavaSqlDate(dateREVto.getValue()));
				if(!orders.isEmpty()) {
					tabREVOrder.setItems(orders);
					tabREVReturn.setItems(orderReturns);
					for(Sale order : orders) {
						totalOrderAmount = totalOrderAmount + order.getTotalPayable();
						totalOrderProfit = totalOrderProfit + order.getTotalProfit();
					}

					for(SaleReturn orderReturn : orderReturns) {
						totalReturnAmount = totalReturnAmount + orderReturn.getTotalRetailPrice();
						totalReturnProfit = totalReturnProfit + orderReturn.getTotalReturnProfit();
					}
					lblTotalOrderAmount.setText(Util.decimalTwoDigitsString(totalOrderAmount));
					lblTotalOrderProfit.setText(Util.decimalTwoDigitsString(totalOrderProfit));
					lblTotalReturnAmount.setText(Util.decimalTwoDigitsString(totalReturnAmount));
					lblTotalReturnProfit.setText(Util.decimalTwoDigitsString(totalReturnProfit));
					
					lblTotalAmount.setText(Util.decimalTwoDigitsString(totalOrderAmount - totalReturnAmount));
					lblTotalProfit.setText(Util.decimalTwoDigitsString(totalOrderProfit - totalReturnProfit));
				}
				else {
					Dialog.information("No Record Found on selected Dates");
				}
			}

		});
	}

}