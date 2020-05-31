package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import interfaces.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Doctor;
import utils.Dialog;
import utils.Util;

public class DoctorUpdateController implements Initializable{
	@FXML
	private BorderPane rootDoctorUpdate;
	

    @FXML
    private HBox barColor;

    @FXML
    private JFXButton btnCross;
    
	@FXML
	private JFXButton btnUpdate;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXTextField txtName;

	@FXML
	private JFXComboBox<String> cmbIdentity;

	@FXML
	private JFXTextField txtAddress;

	@FXML
	private JFXTextField txtEmail;

	@FXML
	private JFXTextField txtPhone;

	@FXML
	private JFXTextField txtAffiliation;

	private DataBase dataBase;
	private Notification noti;
	private Doctor doctor;

	private ObservableList <String> identity = FXCollections.observableArrayList("Male","Female");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cmbIdentity.setItems(identity);
		Util.checkName(txtName);
		Util.checkNumber(txtPhone);
		Util.numberLimit(txtPhone);
		Util.emailCheck(txtEmail);
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
				&& Util.isValid(txtAddress) && Util.isValid(txtPhone)
				&& Util.isValid(txtAffiliation)) {
			doctor.setDocName(txtName.getText());
			doctor.setDocIdentity(cmbIdentity.getValue());
			doctor.setDocAddress(txtAddress.getText());
			doctor.setDocPhone(txtPhone.getText());
			doctor.setDocEmail(txtEmail.getText());
			doctor.setDocAffiliation(txtAffiliation.getText());
			doctor.setDocEmployee(LoginScreenController.emp);
			dataBase.update(doctor);
			if(noti != null) {
				noti.showNotification("Doctor Updated Successfully");
			}
			Util.hideWindow(event);

		}
	}
	
    @FXML
    void btnCrossAction(ActionEvent event) {
    	rootDoctorUpdate.getScene().getWindow().hide();

    }
   
	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;

	}

	public void setDoctor(Doctor doc) {
		doctor = doc;
		txtName.setText(doctor.getDocName());
		cmbIdentity.setValue(doctor.getDocIdentity());
		txtAddress.setText(doctor.getDocAddress());
		txtPhone.setText(doctor.getDocPhone());
		txtEmail.setText(doctor.getDocEmail());
		txtAffiliation.setText(doctor.getDocAffiliation());
	}

}
