package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import database.PrescriptionDetailDB;
import database.StockDB;
import interfaces.CustomerInterface;
import interfaces.DoctorInterface;
import interfaces.Notification;
import interfaces.StockInterface;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import model.Customer;
import model.Doctor;
import model.Prescription;
import model.PrescriptionDetail;
import model.Stock;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class PrescriptionRegistrationController<T> implements Initializable{

	@FXML
	private BorderPane rootPrescriptionReg;

	@FXML
	private HBox barColor;

	@FXML
	private JFXTextField txtPatientName;

	@FXML
	private JFXButton btnCustomerName;

	@FXML
	private JFXTextField txtDoctorName;

	@FXML
	private JFXButton btnDoctorName;

	@FXML
	private JFXTextField txtDiease;

	@FXML
	private JFXDatePicker DateCurrentDate;

	@FXML
	private JFXTextField txtSearchMed;

	@FXML
	private JFXButton btnAddMedicine;

	@FXML
	private JFXTextField txtDosage;

	@FXML
	private TableView<PrescriptionDetail> tblPrescriptionRegistration;

	@FXML
	private TableColumn<PrescriptionDetail, Number> colSerial;

	@FXML
	private TableColumn<PrescriptionDetail, String> colId;

	@FXML
	private TableColumn<PrescriptionDetail, Stock> colName;

	@FXML
	private TableColumn<PrescriptionDetail, Number> colDosagePerDay;

	@FXML
	private TableColumn<PrescriptionDetail, PrescriptionDetail> colAction;

	private PrescriptionDetailDB dbPrescriptionDetail;
	private StockDB dbStock;
	private DataBase dbCustomer;


	private DataBase dbDoctor;
	private Notification noti;
	private ObservableList<Stock> stockList;
	private ObservableList<Customer> customersList;
	private ObservableList<Doctor> doctorsList;
	private Stock stock;
	private Customer customer;
	private Doctor doctor;
	private Prescription prescription;
	private PrescriptionDetail prescriptionDetail;
	private ObservableList<PrescriptionDetail> prescriptionDetailList;
	private KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		prescriptionDetailList = FXCollections.observableArrayList();

		DateCurrentDate.setPromptText("MM/dd/yyyy");
		DateCurrentDate.setValue(LocalDate.now());
		setTableCell();
		onSelectDoctor();
		onSelectCustomer();
		onSelectStockDrug();
		enterDosage();
		setAction();
		mouseClick();

		Util.checkName(txtPatientName);
		Util.checkName(txtDoctorName);
		Util.checkName(txtDiease);
		Util.checkDecimalNumber(txtDosage);

	}

	@FXML
	public void btnAddMedicineAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/StockSearch.fxml"));
			Parent root = fxmlLoader.load();
			StockSearchController ssc = (fxmlLoader).getController();
			ssc.sendReference(stockList, new StockInterface() {

				@Override
				public void setStock(Stock stock) {
					setStockDrugObject(stock);
				}
			});

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void btnCustomerNameAction(ActionEvent event){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Customers.fxml"));
			Parent root = fxmlLoader.load();
			CustomersController cc = (fxmlLoader).getController();
			cc.sendReference(dbCustomer, true, new CustomerInterface() {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnDoctorNameAction(ActionEvent event){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Doctors.fxml"));
			Parent root = fxmlLoader.load();
			DoctorsController dc = (fxmlLoader).getController();
			dc.sendReference(dbDoctor, true, new DoctorInterface() {

				@Override
				public void setDoctor(Doctor doctor) {
					setDoctorObject(doctor);
				}
			});

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		if(Dialog.confirmAlert(null,"Are you sure you want to cancel")) {
			Util.hideWindow(event);
		}
	}

	@FXML
	public void btnSaveAction(ActionEvent event) {
		if(Util.isValid(txtPatientName) && Util.isValid(txtDoctorName) 
				&& Util.isValid(txtDiease) && Util.isValid(DateCurrentDate)) {
			if(!prescriptionDetailList.isEmpty()) {
				prescription = new Prescription();
				prescription.setPrescriptionDate(Util.localDateToJavaSqlDate(DateCurrentDate.getValue()));
				prescription.setPrescriptionDisease(txtDiease.getText());
				prescription.setPrescriptionCustomer(customer);
				prescription.setPrescriptionDoctor(doctor);
				prescription.setPrescriptionEmployee(LoginScreenController.emp);
				dbPrescriptionDetail.createPrescription(prescription, prescriptionDetailList);
				if(noti != null) {
					noti.showNotification("Prescription Registered Successfully");
				}
				Util.hideWindow(event);
			}
			else {
				Dialog.error("Select Medicines First");
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void sendReferance(DataBase dbCustomer,DataBase dbDoctor,StockDB dbStock,PrescriptionDetailDB dbPrescriptionDetail, Notification noti) {
		this.dbCustomer = dbCustomer;
		this.dbDoctor = dbDoctor;
		this.dbStock = dbStock;
		this.dbPrescriptionDetail = dbPrescriptionDetail;
		this.noti = noti;
		stockList = (ObservableList<Stock>) ((ObservableList<T>)this.dbStock.getList());
		TextFields.bindAutoCompletion(txtSearchMed, stockList);
		customersList = (ObservableList<Customer>) ((ObservableList<T>)this.dbCustomer.getList());
		TextFields.bindAutoCompletion(txtPatientName, customersList);
		doctorsList = (ObservableList<Doctor>) ((ObservableList<T>)this.dbDoctor.getList());
		TextFields.bindAutoCompletion(txtDoctorName, doctorsList);

	}

	private void setTableCell() {
		tblPrescriptionRegistration.setEditable(true);
		colSerial.setCellFactory(new Callback<TableColumn<PrescriptionDetail,Number>, TableCell<PrescriptionDetail,Number>>() {

			@Override
			public TableCell<PrescriptionDetail, Number> call(TableColumn<PrescriptionDetail, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colId.setCellValueFactory(data -> data.getValue().prescriptionDetailDrugProperty().get().getStockDrug().drugUPCProperty());
		colName.setCellValueFactory(data -> data.getValue().prescriptionDetailDrugProperty());
		colDosagePerDay.setCellValueFactory(data -> data.getValue().prescriptionDetailDosageProperty());
		colDosagePerDay.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tblPrescriptionRegistration.setItems(prescriptionDetailList);
	}

	private void onSelectStockDrug() {
		txtSearchMed.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtSearchMed.getText().isEmpty()) {
					for(Stock sd : stockList) {
						if(sd.getStockDrug().toString().equals(txtSearchMed.getText())) {
							stock = sd;
							return;
						}
					}
					txtSearchMed.clear();
					txtSearchMed.requestFocus();
					Dialog.error("This Product is Not Registered");
					return;
				}
			}
		});
	}

	private void onSelectCustomer() {
		txtPatientName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtPatientName.getText().isEmpty()) {
					for(Customer c : customersList) {
						if(c.getCustName().equals(txtPatientName.getText())) {
							customer = c;
							return;
						}
						if(c.toString().equals(txtPatientName.getText())) {
							customer = c;
							return;
						}
					}
					txtPatientName.clear();
					txtPatientName.requestFocus();
					Dialog.error("This Customer is Not Registered");
					return;
				}
			}
		});
	}

	private void onSelectDoctor() {
		txtDoctorName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtDoctorName.getText().isEmpty()) {
					for(Doctor d : doctorsList) {
						if(d.getDocName().equals(txtDoctorName.getText())) {
							doctor = d;
							return;
						}
					}

					txtDoctorName.clear();
					txtDoctorName.requestFocus();
					Dialog.error("This Doctor is Not Registered");
					return;
				}
			}
		});
	}

	private void enterDosage() {
		txtDosage.setOnKeyPressed(event ->{
			if(enter.match(event)) {  
				if(!txtDosage.getText().isEmpty()) {
					prescriptionDetail = new PrescriptionDetail();
					prescriptionDetail.setPrescriptionDetailDrug(stock);
					prescriptionDetail.setPrescriptionDetailDosage(Double.valueOf(txtDosage.getText()));
					for(PrescriptionDetail p : prescriptionDetailList) {
						if(p.getPrescriptionDetailDrug().equals(prescriptionDetail.getPrescriptionDetailDrug())) {
							p.setPrescriptionDetailDosage(p.getPrescriptionDetailDosage() + prescriptionDetail.getPrescriptionDetailDosage());
							txtDosage.clear();
							txtSearchMed.clear();
							txtSearchMed.requestFocus();
							return;
						}
					}
					prescriptionDetailList.add(prescriptionDetail);
					txtDosage.clear();
					txtSearchMed.clear();
					txtSearchMed.requestFocus();
				}
			}
		});
	}

	private void mouseClick() {
		tblPrescriptionRegistration.setRowFactory(tr -> {
			TableRow<PrescriptionDetail> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblPrescriptionRegistration.getSelectionModel().clearSelection();
				}
			});

			return row;
		});
	}


	private void setAction() {

		colAction.setCellFactory(param -> new TableCell<PrescriptionDetail, PrescriptionDetail>() {
			@Override
			protected void updateItem(PrescriptionDetail prescriptionDetail, boolean empty) {
				super.updateItem(prescriptionDetail, empty);

				if (prescriptionDetail == null) {
					setGraphic(null);
					return;
				}
				HBox hb;
				Button deleteButton;
				hb = new HBox();

				hb.setPadding(new Insets(0, 3, 0, 3));
				//15, 12, 15, 12
				hb.setSpacing(10);
				deleteButton = new Button();

				Image image = new Image("/media/delete.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);
				deleteButton.setGraphic(imageView);
				deleteButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(deleteButton);
				hb.setAlignment(Pos.CENTER);
				setGraphic(hb);

				deleteButton.setOnAction(
						event -> {
							if(Dialog.confirmAlert(null, "Are you Sure! you want to delete")){
								prescriptionDetailList.remove(prescriptionDetail);
								tblPrescriptionRegistration.requestFocus();
							}
						}
						);
			}
		});
	}

	private void setStockDrugObject(Stock stock) {
		this.stock = stock;
		txtSearchMed.setText(this.stock.toString());
		txtDosage.requestFocus();
	}

	private void setCustomerObject(Customer customer) {
		this.customer = customer;
		txtPatientName.setText(this.customer.toString());
		txtDoctorName.requestFocus();
	}

	private void setDoctorObject(Doctor doctor) {
		this.doctor = doctor;
		this.txtDoctorName.setText(this.doctor.toString());
		txtDiease.requestFocus();
	}
	
	@FXML
    void btnCrossAction(ActionEvent event) {
		rootPrescriptionReg.getScene().getWindow().hide();
    }


}
