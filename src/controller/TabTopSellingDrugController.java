package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.ReportTopSellingDrug;
import model.Stock;
import utils.Dialog;
import utils.Util;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import database.ReportsDB;
import javafx.scene.control.TableColumn;

public class TabTopSellingDrugController implements Initializable{
	@FXML
	private BorderPane root;

	@FXML
	private JFXDatePicker dateTSDfrom;

	@FXML
	private JFXDatePicker dateTSDto;

	@FXML
	private TableView<ReportTopSellingDrug> tabTSD;

	@FXML
	private TableColumn<ReportTopSellingDrug, String> colID;

	@FXML
	private TableColumn<ReportTopSellingDrug, String> colProductName;

	@FXML
	private TableColumn<ReportTopSellingDrug, Number> colTotalOrder;

	@FXML
	private TableColumn<ReportTopSellingDrug, Number> colTotalItem;

	@FXML
	private TableColumn<ReportTopSellingDrug, Number> colPrice;

	@FXML
	private TableColumn<ReportTopSellingDrug, Number> colProfit;

	private List<Object[]> list;
	private ObservableList<ReportTopSellingDrug> reports = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.setDatePicker(dateTSDfrom);
		dateTSDfrom.setEditable(false);
		dateTSDto.setEditable(false);
		onDateFrom();
		onDateTo();
		setTableCell();
		tabTSD.setItems(reports);
	}

	private void setTableCell() {
		colID.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().stockDrugProperty().get().drugUPCProperty());
		colProductName.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().stockDrugProperty().get().drugCommonNameProperty());
		colTotalOrder.setCellValueFactory(data -> data.getValue().totalOrdersProperty());
		colTotalItem.setCellValueFactory(data -> data.getValue().totalQtyProperty());
		colPrice.setCellValueFactory(data -> data.getValue().totalRetailPriceProperty());
		colProfit.setCellValueFactory(data -> data.getValue().totalProfitProperty());
		
		colPrice.setStyle("-fx-alignment: CENTER-RIGHT;");
		colProfit.setStyle("-fx-alignment: CENTER-RIGHT;");
	}

	private void onDateFrom() {
		dateTSDfrom.setOnAction(event ->{
			Util.setDatePicker(dateTSDfrom, dateTSDto);
			dateTSDto.requestFocus();
		});
	}

	private void onDateTo() {
		dateTSDto.setOnAction(event ->{
			if(dateTSDfrom.getValue()== null) {
				Dialog.error("Please Select 'From Date' First");
			}
			else {
				list = ReportsDB.topSelling(Util.localDateToJavaSqlDate(dateTSDfrom.getValue()) , Util.localDateToJavaSqlDate(dateTSDto.getValue()));
				reports.clear();
				if(!list.isEmpty()) {
					for(Object[] r : list) {
						ReportTopSellingDrug rtsd = new ReportTopSellingDrug();
						rtsd.setStockDrug((Stock) r[0]);
						rtsd.setTotalOrders(Integer.valueOf(r[1].toString()));
						rtsd.setTotalQty(Integer.valueOf(r[2].toString()));
						rtsd.setTotalRetailPrice(Double.valueOf(r[3].toString()));
						rtsd.setTotalTradePrice(Double.valueOf(r[4].toString()));
						rtsd.setTotalProfit(rtsd.getTotalRetailPrice() - rtsd.getTotalTradePrice());
						reports.add(rtsd);
					}
				}
				else {
					Dialog.information("No Record Found on selected Dates");
				}
			}
		});
	}


}
