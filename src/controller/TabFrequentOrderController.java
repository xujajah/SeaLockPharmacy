package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.Customer;
import model.ReportFrequentOrder;
import utils.Dialog;
import utils.Util;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

import database.DBUtil;
import database.ReportsDB;
import javafx.scene.control.TableColumn;

public class TabFrequentOrderController implements Initializable{
	@FXML
	private BorderPane root;

	@FXML
	private JFXDatePicker dateFOfrom;

	@FXML
	private JFXDatePicker dateFOto;

	@FXML
	private TableView<ReportFrequentOrder> tabFrequentOrder;

	@FXML
	private TableColumn<ReportFrequentOrder, Number> colID;

	@FXML
	private TableColumn<ReportFrequentOrder, String> colCustomerName;

	@FXML
	private TableColumn<ReportFrequentOrder, String> colTotalOrder;

	private List<Object[]> list;
	private ObservableList<ReportFrequentOrder> reports = FXCollections.observableArrayList();
	Comparator<? super ReportFrequentOrder> comparator;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.setDatePicker(dateFOfrom);
		dateFOfrom.setEditable(false);
		dateFOto.setEditable(false);
		setTableCell();
		onDateFrom();
		onDateTo();
		comparator = new Comparator<ReportFrequentOrder>() {
			@Override
			public int compare(ReportFrequentOrder o1, ReportFrequentOrder o2) {
				return o2.getTotalOrders().compareToIgnoreCase(o1.getTotalOrders());
			}
		};
	}

	private void setTableCell() {

		colID.setCellValueFactory(data -> data.getValue().customerProperty().get().custIdProperty());
		colCustomerName.setCellValueFactory(data -> data.getValue().customerProperty().get().custNameProperty());
		colTotalOrder.setCellValueFactory(data -> data.getValue().totalOrdersProperty());
		tabFrequentOrder.setItems(reports);
	}

	private void onDateFrom() {
		dateFOfrom.setOnAction(event ->{
			Util.setDatePicker(dateFOfrom, dateFOto);
			dateFOto.requestFocus();
		});
	}

	private void onDateTo() {
		dateFOto.setOnAction(event ->{
			if(dateFOfrom.getValue()== null) {
				Dialog.error("Please Select 'From Date' First");
			}
			else {
			list = ReportsDB.customerOrders(Util.localDateToJavaSqlDate(dateFOfrom.getValue()) , Util.localDateToJavaSqlDate(dateFOto.getValue()) );
			reports.clear(); 
			if(!list.isEmpty()) {

				for (Object[] r : list){
					Customer cust = (Customer) DBUtil.getObject(Customer.class, "custId", (int) r[0]);
					ReportFrequentOrder report = new ReportFrequentOrder();
					report.setCustomer(cust);
					report.setTotalOrders(r[1].toString());
					reports.add(report);
					Collections.sort(reports, comparator);
				}	
			}
			else {
				Dialog.information("No Record Found on selected Dates");
			}
			}
		});
	}

}
