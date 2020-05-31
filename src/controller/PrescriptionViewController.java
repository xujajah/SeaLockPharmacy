package controller;

import java.net.URL;

import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import database.PrescriptionDB;
import database.PrescriptionDetailDB;
import database.StockDB;
import interfaces.CustomerInterface;
import interfaces.Notification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Customer;
import model.Doctor;
import model.Prescription;
import model.PrescriptionDetail;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class PrescriptionViewController implements Initializable {


	@FXML
	private BorderPane rootPrescriptionView;

	@FXML
	private HBox barColor;

	@FXML
	private JFXTextField txtSearchName;

	@FXML
	private JFXButton btnSearchName;

	@FXML
	private JFXComboBox<Prescription> cmbSearchDate;

	@FXML
	private Label lblPatientName;

	@FXML
	private Label lblDiease;

	@FXML
	private Label lblDoctorName;

	@FXML
	private Label lblDate;

	@FXML
	private TableView<PrescriptionDetail> tblPrescriptionView;

	@FXML
	private TableColumn<PrescriptionDetail, Number> colSerial;

	@FXML
	private TableColumn<PrescriptionDetail, String> colPid;

	@FXML
	private TableColumn<PrescriptionDetail, String> colName;

	@FXML
	private TableColumn<PrescriptionDetail, Number> colDosagePerDay;







	private PrescriptionDB dbPrescription = new PrescriptionDB();
	private PrescriptionDetailDB dbPrescriptionDetail = new PrescriptionDetailDB();
	private DataBase dbCustomer = new DataBase();
	private DataBase dbDoctor = new DataBase();
	private StockDB dbStock = new StockDB();
	private ObservableList<Customer> customersList;
	private ObservableList<Prescription> prescriptionList;
	private ObservableList<PrescriptionDetail> prescriptionDetailList;
	private Customer customer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customersList = dbCustomer.retrieve(Customer.class,"custActive",true);
		dbDoctor.retrieve(Doctor.class,"docActive",true);
		dbStock.stockReterive();
		TextFields.bindAutoCompletion(txtSearchName, customersList);
		onSelectCustomer();
		prescriptionSelect();
		setTableCell();
		mouseClick();
	}

	@FXML
	public void btnAddAction(ActionEvent event){
		try {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrescriptionRegistration.fxml"));
		Parent root = fxmlLoader.load();
		@SuppressWarnings("rawtypes")
		PrescriptionRegistrationController prc = (fxmlLoader).getController();
		prc.sendReferance(dbCustomer,dbDoctor ,dbStock, dbPrescriptionDetail, new Notification() {

			@Override
			public void showNotification(String msg) {
				Util.showNotification(rootPrescriptionView, msg);
			}
		});
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnSearchNameAction(ActionEvent event){
		try {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Customers.fxml"));
		Parent root = fxmlLoader.load();
		CustomersController cc = (fxmlLoader).getController();
		cc.sendReference(dbCustomer, false, new CustomerInterface() {

			@Override
			public void setCustomer(Customer customer) {
				setCustomerObject(customer);
			}
		});
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<PrescriptionDetail,Number>, TableCell<PrescriptionDetail,Number>>() {

			@Override
			public TableCell<PrescriptionDetail, Number> call(TableColumn<PrescriptionDetail, Number> arg0) {
				return new SerialCell<>();
			}
		});

		colSerial.setStyle("-fx-alignment: CENTER;");

		colPid.setCellValueFactory(data -> data.getValue().prescriptionDetailDrugProperty().get().stockDrugProperty().get().drugUPCProperty());
		colName.setCellValueFactory(data -> data.getValue().prescriptionDetailDrugProperty().get().stockDrugProperty().get().drugCommonNameProperty());
		colDosagePerDay.setCellValueFactory(data -> data.getValue().prescriptionDetailDosageProperty());

	}

	private void onSelectCustomer() {
		txtSearchName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtSearchName.getText().isEmpty()) {
					for(Customer c : customersList) {
						if(c.toString().equals(txtSearchName.getText())) {
							customer = c;

							prescriptionList = dbPrescription.customerPrescriptions(customer.getCustId());
							cmbSearchDate.setItems(prescriptionList);
							return;
						}
					}
					txtSearchName.clear();
					txtSearchName.requestFocus();
					Dialog.error("This Customer is Not Registered");
				}
			}
		});
	}


	private void prescriptionSelect() {

		cmbSearchDate.valueProperty().addListener(new ChangeListener<Prescription>() {

			@Override
			public void changed(ObservableValue<? extends Prescription> observable, Prescription oldValue,
					Prescription newValue) {
				for(Prescription p : prescriptionList) {
					if(newValue.equals(p)) {
						prescriptionDetailList = dbPrescriptionDetail.getPrescriptionDetail(p.getPrescriptionId());
						lblPatientName.setText(p.getPrescriptionCustomer().getCustName());
						lblDoctorName.setText(p.getPrescriptionDoctor().getDocName());
						lblDiease.setText(p.getPrescriptionDisease());
						lblDate.setText(p.getPrescriptionDate().toString());
						tblPrescriptionView.setItems(prescriptionDetailList);
					}
				}
			}
		});
	}

	
	private void setCustomerObject(Customer customer) {
		this.customer = customer;
		txtSearchName.setText(this.customer.toString());
		prescriptionList = dbPrescription.customerPrescriptions(customer.getCustId());
		cmbSearchDate.setItems(prescriptionList);
		cmbSearchDate.requestFocus();
	}

	private void mouseClick() {
		tblPrescriptionView.setRowFactory(tr -> {
			TableRow<PrescriptionDetail> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblPrescriptionView.getSelectionModel().clearSelection();
				}
			});

			return row;
		});
	}


}
