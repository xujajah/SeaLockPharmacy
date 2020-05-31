package controller;


import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import interfaces.Notification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Supplier;
import utils.Dialog;
import utils.Util;

public class SupplierRegistrationController implements Initializable{

	@FXML
	private BorderPane rootSupplierReg;
	
    @FXML
    private HBox barColor;
    
    @FXML
    private JFXButton btnCross;

	@FXML
	private JFXTextField txtSupplierName;

	@FXML
	private JFXTextField txtPhone;

	@FXML
	private JFXTextField txtEmail;

	@FXML
	private JFXTextField txtwebsite;

	@FXML
	private JFXTextField txtAddress;
	private DataBase dataBase;
	private Notification noti;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.checkName(txtSupplierName);
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
		if(Util.isValid(txtSupplierName) && Util.isValid(txtPhone) && Util.isValid(txtAddress)) {
			if(DBUtil.uniqueResult(Supplier.class, "suppName", txtSupplierName.getText(), "suppPhone", txtPhone.getText())) {
				Supplier supplier = new Supplier();
				supplier.setSuppAddress(txtAddress.getText());
				supplier.setSuppEmail(txtEmail.getText() );
				supplier.setSuppName(txtSupplierName.getText());
				supplier.setSuppWebsite(txtwebsite.getText());
				supplier.setSuppPhone(txtPhone.getText());
				supplier.setSuppEmployee(LoginScreenController.emp);
				supplier.setSuppActive(true);
				dataBase.create(supplier);
				if(noti != null) {
					noti.showNotification("Supplier Registered Successfully");
				}
				Util.hideWindow(event);
			}
			else {
				Dialog.error("Duplicate Entry For Supplier Name and Phone Number");
			}
		}
	}

	@FXML
    void btnCrossAction(ActionEvent event) {
		rootSupplierReg.getScene().getWindow().hide();
    }

    
	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;
	}

}
