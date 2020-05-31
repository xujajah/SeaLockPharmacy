package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TabViewReportController implements Initializable{
	@FXML
	private BorderPane rootReport;
	
	@FXML
	private HBox barColor;
	@FXML
	private TabPane tabPane;
	
	@FXML
	private TabFrequentOrderController tabFrequentOrderController;
	
	@FXML
	private TabTopSellingDrugController tabTopSellingDrugController;
	@FXML
	private TabPopularMedicineController tabPopularMedicineController;
	@FXML
	private ExpiryViewController  expiryViewController;
	@FXML
	private TabRevenueController tabRevenueController;
	@FXML
	private TabLoginHistoryController tabLoginHistoryController;
	@FXML
	private TabDailyReportController tabDailyReportController;
	@FXML
	private TabWeeklyMonthlyReportController tabWeeklyMonthlyReportController;
	
	@FXML
	private TabControlDrugController tabControlDrugController;
	
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
