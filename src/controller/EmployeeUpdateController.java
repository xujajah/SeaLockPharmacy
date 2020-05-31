package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import interfaces.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Employee;
import utils.Dialog;
import utils.Util;

public class EmployeeUpdateController implements Initializable {
	@FXML
	private BorderPane rootEmployeeUpdate;

	@FXML
	private HBox barColor;

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
	private JFXTextField txtCnic;

	@FXML
	private JFXTextField txtPhone;

	@FXML
	private JFXTextField txtEmail;

	@FXML
	private JFXDatePicker dateDOB;

	@FXML
	private JFXComboBox<String> cmbDesignation;

	@FXML
	private JFXTextField txtUserName;

	@FXML
	private JFXTextField txtPswd;

	private Employee employee;
	private DataBase dataBase;
	private Notification noti;

	private ObservableList <String> identity = FXCollections.observableArrayList("Male","Female");
	private ObservableList <String> designation = FXCollections.observableArrayList("Stock Manager","Sales Manager","General Manager","Admin");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateDOB.setPromptText("mm/dd/yyyy");
		cmbIdentity.setItems(identity);
		cmbDesignation.setItems(designation);
		Util.checkName(txtName);
		Util.checkNumber(txtPhone);
		Util.numberLimit(txtPhone);
		Util.checkCNIC(txtCnic);
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
				&& Util.isValid(txtAddress) && Util.isValid(txtCnic)
				&& Util.isValid(txtPhone)
				&& Util.isValid(dateDOB)
				&& Util.isValid(cmbDesignation)
				&& Util.isValid(txtUserName) && Util.isValid(txtPswd)) {
			if(DBUtil.uniqueResultUpdate(Employee.class,"empId",employee.getEmpId(), "empName", txtName.getText(), "empPhone", txtPhone.getText())) {
				if(DBUtil.uniqueResultUpdate(Employee.class, "empId",employee.getEmpId(),"empCNIC", txtCnic.getText())) {
					if(DBUtil.uniqueResultUpdate(Employee.class,"empId",employee.getEmpId(), "empUserName", txtUserName.getText())) {
						employee.setEmpName(txtName.getText());
						employee.setEmpIdentity(cmbIdentity.getValue());
						employee.setEmpAddress(txtAddress.getText());
						employee.setEmpCNIC(txtCnic.getText());
						employee.setEmpPhone(txtPhone.getText());
						employee.setEmpDOB(Util.localDateToJavaSqlDate(dateDOB.getValue()));
						employee.setEmpEmail(txtEmail.getText());
						employee.setEmpDesignation(cmbDesignation.getValue());
						employee.setEmpUserName(txtUserName.getText());
						employee.setEmpPswd(txtPswd.getText());
						dataBase.update(employee);
						if(noti != null) {
							noti.showNotification("Employee Updated Successfully");
						}
						Util.hideWindow(event);
					}else {
						Dialog.error("Username Already Exist");
					}
				}else {
					Dialog.error("Duplicate Entry For CNIC");
				}
			}else {
				Dialog.error("Duplicate Entry for Employee");
			}
		}
	}
	 @FXML
	    void btnCrossAction(ActionEvent event) {
		 rootEmployeeUpdate.getScene().getWindow().hide();
	    }
	    
	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;
	}

	public void setEmployee(Employee emp) {
		employee = emp;
		txtName.setText(employee.getEmpName());
		cmbIdentity.setValue(employee.getEmpIdentity());
		txtAddress.setText(employee.getEmpAddress());
		txtCnic.setText(employee.getEmpCNIC());
		txtPhone.setText(employee.getEmpPhone());
		dateDOB.setValue(Util.utilDateToLocalDate(employee.getEmpDOB()));
		txtEmail.setText(employee.getEmpEmail());
		cmbDesignation.setValue(employee.getEmpDesignation());
		txtUserName.setText(employee.getEmpUserName());
		txtPswd.setText(employee.getEmpPswd());	


	}

}
