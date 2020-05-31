package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.SaleDetailDB;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.SaleDetail;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabControlDrugController implements Initializable{

	@FXML
	private BorderPane root;

	@FXML
	private JFXTextField txtSearch;

	@FXML
	private JFXDatePicker dateFrom;

	@FXML
	private JFXDatePicker dateTo;

	@FXML
	private TableView<SaleDetail> tabTSD;

	@FXML
	private TableColumn<SaleDetail, Number> colSNo;

	@FXML
	private TableColumn<SaleDetail, String> colProductName;

	@FXML
	private TableColumn<SaleDetail, String> colCustomerName;

	@FXML
	private TableColumn<SaleDetail, String> colCustomerPhone;

	@FXML
	private TableColumn<SaleDetail, String> colDoctorName;

	@FXML
	private TableColumn<SaleDetail, Number> colQty;

	@FXML
	private TableColumn<SaleDetail, Date> colDate;

	SaleDetailDB dbSaleDetail;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dbSaleDetail = new SaleDetailDB();
		Util.setDatePicker(dateFrom);
		dateFrom.setEditable(false);
		dateTo.setEditable(false);
		onDateFrom();
		onDateTo();
		setTableCell();
	}

	private void setTableCell() {
		colSNo.setCellFactory(new Callback<TableColumn<SaleDetail,Number>, TableCell<SaleDetail,Number>>() {

			@Override
			public TableCell<SaleDetail, Number> call(TableColumn<SaleDetail, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colProductName.setCellValueFactory(data -> data.getValue().saleStockDrugProperty().get().stockDrugProperty().get().drugCommonNameProperty());
		colCustomerName.setCellValueFactory(data -> data.getValue().saleIdProperty().get().saleCustomerProperty().get().custNameProperty());
		colCustomerPhone.setCellValueFactory(data -> data.getValue().saleIdProperty().get().saleCustomerProperty().get().custPhoneProperty());
		colDoctorName.setCellValueFactory(data -> data.getValue().saleIdProperty().get().saleDoctorProperty().get().docNameProperty());
		colQty.setCellValueFactory(data -> data.getValue().saleQtyProperty());
		colDate.setCellValueFactory(data -> data.getValue().saleIdProperty().get().saleDateProperty());

	}

	private void onDateFrom() {
		dateFrom.setOnAction(event ->{
			Util.setDatePicker(dateFrom, dateTo);
			dateTo.requestFocus();
		});
	}

	private void onDateTo() {
		dateTo.setOnAction(event ->{
			if(dateFrom.getValue()== null) {
				Dialog.error("Please Select 'From Date' First");
			}
			else {
				filterSearch();
				txtSearch.requestFocus();
			}
		});
	}
	
	private void filterSearch() {
		ObservableList<SaleDetail> controlDrugList = dbSaleDetail.getControlDrugSale(Util.localDateToJavaSqlDate(dateFrom.getValue()), Util.localDateToJavaSqlDate(dateTo.getValue()));
		FilteredList<SaleDetail> filteredList = new FilteredList<>(controlDrugList, p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(controlDrug -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return controlDrug.getSaleId().getSaleDoctor().getDocName().toLowerCase().contains(lowerCaseFilter)
						|| controlDrug.getSaleId().getSaleCustomer().getCustName().toLowerCase().contains(lowerCaseFilter)
						|| controlDrug.getSaleId().getSaleCustomer().getCustPhone().toLowerCase().contains(lowerCaseFilter)
						|| controlDrug.getSaleStockDrug().getStockDrug().getDrugCommonName().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<SaleDetail> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tabTSD.comparatorProperty());
		tabTSD.setItems(sortedList);
		if(controlDrugList.isEmpty()) {
			Dialog.information("No Record Found On Selected Date");
		}
	}


}
