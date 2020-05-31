package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import database.RegionDB;
import interfaces.Notification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Customer;
import model.Province;
import model.Region;
import utils.Dialog;
import utils.Util;

public class CustomerRegistrationController implements Initializable{

	@FXML
	private BorderPane rootCustomerReg;

	@FXML
	private HBox barColor;

	@FXML
	private JFXButton btnCross;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXTextField txtName;

	@FXML
	private JFXComboBox<String> cmbIdentity;

	@FXML
	private JFXTextField txtAddress;

	@FXML
	private JFXComboBox<Province> cmbProvince;

	@FXML
	private JFXComboBox<Region> cmbRegion;

	@FXML
	private JFXTextField txtEmail;

	@FXML
	private JFXTextField txtPhone;

	@FXML
	private JFXDatePicker dateDOB;

	@FXML
	private JFXButton btnAddRegion;

	@FXML
	private JFXButton btnAddProvince;
	
	
	private DataBase dataBase;
	private Notification noti;
	private DataBase provinceDB;
	private RegionDB regionDB;
	private Province province;


	private ObservableList <String> identity = FXCollections.observableArrayList("Male","Female");
	private ObservableList <Province> provinceList;
	private ObservableList <Region> regionList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		provinceDB = new DataBase();
		regionDB = new RegionDB();
		provinceList = provinceDB.retrieve(Province.class);
		province = new Province();
		dateDOB.setPromptText("yyyy/mm/dd");
		cmbIdentity.setItems(identity);
		cmbProvince.setItems(provinceList);
		Util.checkName(txtName);
		Util.checkNumber(txtPhone);
		Util.numberLimit(txtPhone);
		Util.emailCheck(txtEmail);
		Util.setDatePicker(dateDOB);
		onSelectProvince();
	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		if(Dialog.confirmAlert(null,"Are you sure you want to cancel")) {
			Util.hideWindow(event);
		}
	}

	@FXML
	public void btnSaveAction(ActionEvent event) {
		if(Util.isValid(txtName) && Util.isValid(cmbIdentity)
				&& Util.isValid(txtAddress)
				&& Util.isValid(cmbProvince) && Util.isValid(cmbRegion)
				&& Util.isValid(txtPhone)
				&& Util.isValid(dateDOB)
				){
			if(DBUtil.uniqueResult(Customer.class, "custName", txtName.getText(), "custPhone", txtPhone.getText())) {
				Customer customer = new Customer();
				customer.setCustName(txtName.getText());
				customer.setCustIdentity(cmbIdentity.getValue());
				customer.setCustAddress(txtAddress.getText());
				customer.setCustRegion(cmbRegion.getValue());
				customer.setCustEmail(txtEmail.getText());
				customer.setCustPhone(txtPhone.getText());
				customer.setCustDOB(Util.localDateToJavaSqlDate(dateDOB.getValue()));
				customer.setCustEmployee(LoginScreenController.emp);
				customer.setCustActive(true);
				dataBase.create(customer);
				if(noti != null) {
					noti.showNotification("Customer Registered Successfully");
				}
				Util.hideWindow(event);
			}
			else {
				Dialog.error("Duplicate Entry for Customer");
			}
		}
	}

	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;
	}
	
	private void onSelectProvince() {
		cmbProvince.valueProperty().addListener(new ChangeListener<Province>() {

			@Override
			public void changed(ObservableValue<? extends Province> observable, Province oldValue, Province newValue) {
				for(Province p : provinceList) {
					if(newValue.equals(p)) {
						province = p;
						regionList = regionDB.getRegions(p.getProvinceId());
						cmbRegion.setItems(regionList);
					}
				}
			}
		});
	}
	
	@FXML
	public void btnAddProvinceAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Province.fxml"));
			Parent root = fxmlLoader.load();
			ProvinceController pc = (fxmlLoader).getController();
			pc.sendReference(provinceDB);
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
	public void btnAddRegionAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Region.fxml"));
			Parent root = fxmlLoader.load();
			RegionController rc = (fxmlLoader).getController();
			rc.sendReference(regionDB,province);
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
	public void btnCrossAction(ActionEvent event) {
		rootCustomerReg.getScene().getWindow().hide();
	}
}
