package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Company;
import utils.Util;

public class CompanyRegistrationController implements Initializable{

	@FXML
	private BorderPane rootCompanyReg;

	@FXML
	private HBox barColor;

	@FXML
	private JFXButton btnCross;

	@FXML
	private JFXTextField txtCompanyName;

	@FXML
	private JFXTextField txtCompanyAddress;

	@FXML
	private JFXTextField txtCompanyCity;

	@FXML
	private TextArea txtBillNotes;

	@FXML
	private JFXTextField txtCompanyPhone;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	private Company company;
	private DataBase database;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		database = new DataBase();
		company = new Company();
		company = (Company) DBUtil.getObject(Company.class, 1);
		txtCompanyName.setText(company.getName());
		txtCompanyAddress.setText(company.getAddress());
		txtCompanyCity.setText(company.getCity());
		txtCompanyPhone.setText(company.getPhone());
		txtBillNotes.setText(company.getBillNotes());

	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		Util.hideWindow(event);
	}

	@FXML
	public void btnCrossAction(ActionEvent event) {
		Util.hideWindow(event);
	}

	@FXML
	public void btnSaveAction(ActionEvent event) {
		company.setName(txtCompanyName.getText());
		company.setAddress(txtCompanyAddress.getText());
		company.setCity(txtCompanyCity.getText());
		company.setPhone(txtCompanyPhone.getText());
		company.setBillNotes(txtBillNotes.getText());
		database.update(company);
		Util.showNotification(rootCompanyReg, "Company Updated Successfully");
	}



}
