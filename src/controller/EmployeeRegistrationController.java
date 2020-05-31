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
import model.Theme;
import utils.Dialog;
import utils.Util;

public class EmployeeRegistrationController implements Initializable {
	@FXML
	private BorderPane rootEmployeeReg;  

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
    void btnCrossAction(ActionEvent event) {
    	rootEmployeeReg.getScene().getWindow().hide();
    }
    
	@FXML
	public void btnSaveAction(ActionEvent event) {
		if(Util.isValid(txtName) && Util.isValid(cmbIdentity)
				&& Util.isValid(txtAddress) && Util.isValid(txtCnic)
				&& Util.isValid(txtPhone)
				&& Util.isValid(dateDOB)
				&& Util.isValid(cmbDesignation)
				&& Util.isValid(txtUserName) && Util.isValid(txtPswd)) {
			if(DBUtil.uniqueResult(Employee.class, "empName", txtName.getText(), "empPhone", txtPhone.getText())) {
				if(DBUtil.uniqueResult(Employee.class, "empCNIC", txtCnic.getText())) {
					if(DBUtil.uniqueResult(Employee.class, "empUserName", txtUserName.getText())) {
						Employee employee = new Employee();
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
						employee.setEmpActive(true);
						dataBase.create(employee);
						Theme theme = new Theme();
						theme.setThemeId(1);
						theme.setEmployeeTheme(employee);
						new DataBase().create(theme);
						if(noti != null) {
							noti.showNotification("Employee Registered Successfully");
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

	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;
	}

}
