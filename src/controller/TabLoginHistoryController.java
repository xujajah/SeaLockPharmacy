package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.ReportsDB;
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
import model.LoginHistory;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabLoginHistoryController implements Initializable{

	@FXML
	private BorderPane rootLoginHistory;

	@FXML
	private JFXTextField txtsearch;

	@FXML
	private JFXDatePicker dateLoginHistory;

	@FXML
	private TableView<LoginHistory> tabLH;

	@FXML
	private TableColumn<LoginHistory, Number> colSerial;

	@FXML
	private TableColumn<LoginHistory, String> colUserName;

	@FXML
	private TableColumn<LoginHistory, String> colEmployeeName;

	@FXML
	private TableColumn<LoginHistory, String> colDesignation;

	@FXML
	private TableColumn<LoginHistory, String> colStatus;

	@FXML
	private TableColumn<LoginHistory, Date> colAccessDate;

	@FXML
	private TableColumn<LoginHistory, Date> colAccessTime;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dateLoginHistory.setEditable(false);
		Util.setDatePicker(dateLoginHistory);
		dateLoginHistory.requestFocus();
		setTableCell();
		onSelectDate();
	}

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<LoginHistory,Number>, TableCell<LoginHistory,Number>>() {

			@Override
			public TableCell<LoginHistory, Number> call(TableColumn<LoginHistory, Number> arg0) {
				return new SerialCell<>();
			}
		});

		colUserName.setCellValueFactory(data -> data.getValue().employeeProperty().get().empUserNameProperty());
		colEmployeeName.setCellValueFactory(data -> data.getValue().employeeProperty().get().empNameProperty());
		colDesignation.setCellValueFactory(data -> data.getValue().employeeProperty().get().empDesignationProperty());
		colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
		colAccessDate.setCellValueFactory(data -> data.getValue().dateProperty());
		colAccessTime.setCellValueFactory(data -> data.getValue().timeProperty());
	}

	private void filterSearch() {
		ObservableList<LoginHistory> loginHistoryList = ReportsDB.loginHistory(Util.localDateToJavaSqlDate(dateLoginHistory.getValue()));
		FilteredList<LoginHistory> filteredList = new FilteredList<>(loginHistoryList, p -> true);
		txtsearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(loginHistory -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return loginHistory.getEmployee().getEmpDesignation().toLowerCase().contains(lowerCaseFilter)
						|| loginHistory.getEmployee().getEmpUserName().toLowerCase().contains(lowerCaseFilter)
						|| loginHistory.getEmployee().getEmpName().toLowerCase().contains(lowerCaseFilter)
						|| loginHistory.getStatus().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<LoginHistory> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tabLH.comparatorProperty());
		tabLH.setItems(sortedList);
		if(loginHistoryList.isEmpty()) {
			Dialog.information("No Record Found On Selected Date");
		}
	}

	private void onSelectDate() {
		dateLoginHistory.setOnAction(event->{
			txtsearch.requestFocus();
			filterSearch();
		});
	}


}
