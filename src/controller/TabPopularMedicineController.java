package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import database.ReportsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.Stock;
import javafx.scene.control.TableColumn;

public class TabPopularMedicineController implements Initializable{
    @FXML
    private BorderPane root;

    @FXML
    private TableView<Stock> tabPM;

    @FXML
    private TableColumn<Stock, String> colID;

    @FXML
    private TableColumn<Stock, String> colProductName;

    @FXML
    private TableColumn<Stock, String> colMedicalName;

    @FXML
    private TableColumn<Stock, String> colManufacturer;
    
    private List<Object[]> list;
	private ObservableList<Stock> reports = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list = ReportsDB.popularMedicine();
		if(!list.isEmpty()) {
			for(Object[] r : list) {
				Stock stock = (Stock) r[0];
				reports.add(stock);
			}
		}
		setTableCell();
		tabPM.setItems(reports);
	}
	
	private void setTableCell() {
		colID.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().drugUPCProperty());
		colProductName.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().drugCommonNameProperty());
		colMedicalName.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().drugMedicalNameProperty());
		colManufacturer.setCellValueFactory(data -> data.getValue().stockDrugProperty().get().drugManufacturerProperty());
	}

}
