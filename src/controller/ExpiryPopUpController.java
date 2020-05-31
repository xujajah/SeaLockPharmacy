package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.PurchaseDetailDB;
import interfaces.Notification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.PurchaseDetail;
import utils.Util;

public class ExpiryPopUpController implements Initializable{

	@FXML
	private BorderPane mainBorder;

	@FXML
	private HBox barColor;

	@FXML
	private JFXTextField txtMedicineName;

	@FXML
	private JFXTextField txtQuantity;

	@FXML
	private JFXButton btnRemove;

	@FXML
	private JFXButton btnCancel;
	private Notification noti;
	private PurchaseDetailDB dbPurchaseDetail;
	private PurchaseDetail purchaseDetail;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtMedicineName.setEditable(false);
		Util.checkPositiveNumber(txtQuantity);
		txtQuantity.requestFocus();
	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		Util.hideWindow(event);
	}

	@FXML
	public void btnRemoveAction(ActionEvent event) {
		if(Util.isValid(txtMedicineName) && Util.isValid(txtQuantity)) {
			purchaseDetail.setQtyPerUnit(Integer.valueOf(txtQuantity.getText()));
			purchaseDetail.setExpiryActive(false);
			dbPurchaseDetail.expiryDelete(purchaseDetail);
			if(noti != null) {
				noti.showNotification("Expiry removed Successfully");
				Util.hideWindow(event);
			}
		}
	}
	
	public void sendReferance(PurchaseDetail purchaseDetail,PurchaseDetailDB dataBase, Notification noti) {
		this.purchaseDetail = purchaseDetail;
		this.dbPurchaseDetail = dataBase;
		this.noti = noti;
		txtMedicineName.setText(purchaseDetail.getPurchaseDrug().getDrugCommonName());
	}


}
