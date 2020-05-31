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

public class SupplierUpdateController implements Initializable{
	@FXML
	private BorderPane rootSupplierUpdate;

    @FXML
    private HBox barColor;

    @FXML
    private JFXButton btnCross;
    
	@FXML
	private JFXTextField txtSupplierName;

	@FXML
	private JFXTextField txtEmail;

	@FXML
	private JFXTextField txtwebsite;

	@FXML
	private JFXTextField txtAddress;

	@FXML
	private JFXTextField txtPhone;

	private Supplier supplier;	    
	private DataBase dataBase;
	private Notification noti;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Util.checkName(txtSupplierName);
		Util.checkNumber(txtPhone);
		Util.numberLimit(txtPhone);
		Util.emailCheck(txtEmail);
	}

	public void setSupplier(Supplier supp) {
		supplier = supp;

		txtSupplierName.setText(supplier.getSuppName());
		txtAddress.setText(supplier.getSuppAddress());
		txtPhone.setText(supplier.getSuppPhone());
		txtEmail.setText(supplier.getSuppEmail());
		txtwebsite.setText(supplier.getSuppWebsite());
	}

	@FXML
	public  void btnCancelAction(ActionEvent event) {
		if(Dialog.confirmAlert(null,"Are you sure you want to cancel")) {
			Util.hideWindow(event);
		}
	}

	@FXML
	public void btnUpdateAction(ActionEvent event) {
		if(Util.isValid(txtSupplierName) && Util.isValid(txtPhone) && Util.isValid(txtAddress)) {
			if(DBUtil.uniqueResultUpdate(Supplier.class, "suppId", supplier.getSuppId(), "suppName", txtSupplierName.getText(), "suppPhone", txtPhone.getText())) {
				supplier.setSuppAddress(txtAddress.getText());
				supplier.setSuppEmail(txtEmail.getText() );
				supplier.setSuppName(txtSupplierName.getText());
				supplier.setSuppWebsite(txtwebsite.getText());
				supplier.setSuppPhone(txtPhone.getText());
				supplier.setSuppEmployee(LoginScreenController.emp);
				dataBase.update(supplier);
				if(noti != null) {
					noti.showNotification("Supplier Updated Successfully");
				}
				Util.hideWindow(event);
			}else {
				Dialog.error("Duplicate Entry For Supplier Name and Phone Number");
			}
		}

	}
	
    @FXML
    void btnCrossAction(ActionEvent event) {
    	Util.hideWindow(event);

    }
    

	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;

	}


}
