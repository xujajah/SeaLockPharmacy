package controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import model.ReportWeeklyMonthly;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabWeeklyMonthlyReportController implements Initializable{

	@FXML
	private BorderPane rootDailyReport;

	@FXML
	private JFXDatePicker dateFrom;

	@FXML
	private JFXDatePicker dateTo;

	@FXML
	private TableView<ReportWeeklyMonthly> tabWMR;

	@FXML
	private TableColumn<ReportWeeklyMonthly, Number> colSerial;

	@FXML
	private TableColumn<ReportWeeklyMonthly, Date> colDate;

	@FXML
	private TableColumn<ReportWeeklyMonthly, Number> colTotalAmount;

	@FXML
	private TableColumn<ReportWeeklyMonthly, Number> colProfit;

	@FXML
	private Label lblTotalSale;

	@FXML
	private Label lblTotalProfit;

	private ObservableList<ReportWeeklyMonthly> reports =FXCollections.observableArrayList();
	private List<Object[]> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.setDatePicker(dateFrom);
		dateFrom.setEditable(false);
		dateTo.setEditable(false);
		setTableCell();
		tabWMR.setItems(reports);
		onDateFrom();
		onDateTo();
	}

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<ReportWeeklyMonthly,Number>, TableCell<ReportWeeklyMonthly,Number>>() {

			@Override
			public TableCell<ReportWeeklyMonthly, Number> call(TableColumn<ReportWeeklyMonthly, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colDate.setCellValueFactory(data -> data.getValue().dateProperty());
		colTotalAmount.setCellValueFactory(data -> data.getValue().totalPayableProperty());
		colProfit.setCellValueFactory(data -> data.getValue().totalProfitProperty());
		
		colTotalAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
		colProfit.setStyle("-fx-alignment: CENTER-RIGHT;");
	}

	private void onDateFrom() {
		dateFrom.setOnAction(event ->{
			Util.setDatePicker(dateFrom, dateTo);
			dateTo.requestFocus();
		});
	}

	private void onDateTo() {
		dateTo.setOnAction(event ->{
			double totalSale = 0;
			double totalProfit = 0;
			if(dateFrom.getValue() == null) {
				Dialog.error("Please Select 'From Date' First");
			}
			else {
				list = ReportsDB.weeklyMonthly(Util.localDateToJavaSqlDate(dateFrom.getValue()), Util.localDateToJavaSqlDate(dateTo.getValue()));
				reports.clear();
				if(!list.isEmpty()) {
					for(Object[]r : list) {
						ReportWeeklyMonthly rwm = new ReportWeeklyMonthly();
						rwm.setDate((Date) r[0]);
						rwm.setTotalPayable(Double.valueOf(r[1].toString()));
						rwm.setTotalProfit(Double.valueOf(r[2].toString()));
						reports.add(rwm);
						totalSale = totalSale + rwm.getTotalPayable();
						totalProfit = totalProfit + rwm.getTotalProfit();
					}
					lblTotalSale.setText(Util.decimalTwoDigitsString(totalSale));
					lblTotalProfit.setText(Util.decimalTwoDigitsString(totalProfit));
				}
				else {
					lblTotalSale.setText(Util.decimalTwoDigitsString(totalSale));
					lblTotalProfit.setText(Util.decimalTwoDigitsString(totalProfit));
					Dialog.information("No Record Found on selected Dates");
				}
			}
		});
	}

}
