package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import interfaces.Notification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Drug;
import utils.Dialog;
import utils.Util;

public class DrugUpdateController implements Initializable{
    @FXML
    private BorderPane rootDrugUpdate;

    @FXML
    private HBox barColor;

    @FXML
    private JFXButton btnCross;
 
	@FXML
	private JFXTextField txtComName;

	@FXML
	private JFXTextField txtMedName;

	@FXML
	private JFXComboBox<String> cmbCategory;

	@FXML
	private JFXTextField txtUniverCode;

	@FXML
	private JFXTextField txtDosage;

	@FXML
	private JFXTextField txtQtyPerPack;

	@FXML
	private JFXComboBox<String> cmbControlDrug;

	@FXML
	private JFXTextArea txtDescription;

	@FXML
	private JFXTextField txtManufacturer;

	private Drug drug;	
	private DataBase dataBase;
	private Notification noti;
	private ObservableList <String> category = FXCollections.observableArrayList("Pharma","Herbal","Surgical","General","Others");
	private ObservableList <String> controlDrug = FXCollections.observableArrayList("No","Yes");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmbCategory.setItems(category);
		cmbControlDrug.setItems(controlDrug);
		categorySelect();
		Util.checkNumber(txtUniverCode);
		Util.checkPositiveNumber(txtQtyPerPack);
		generateUPC();
	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		if(Dialog.confirmAlert(null,"Are you sure you want to cancel")) {
			Util.hideWindow(event);
		}
	}

	@FXML
	public void btnUpdateAction(ActionEvent event) {
		if(Util.isValid(cmbCategory) && Util.isValid(txtComName) &&
				Util.isValid(txtUniverCode)&&  Util.isValid(txtQtyPerPack)
				&& Util.isValid(cmbControlDrug) && Util.isValid(txtManufacturer)) {
			if(DBUtil.uniqueResultUpdate(Drug.class,"drugId",drug.getDrugId() ,"drugCommonName", txtComName.getText())){
				if(DBUtil.uniqueResultUpdate(Drug.class,"drugId",drug.getDrugId() ,"drugUPC", txtUniverCode.getText())){
					drug.setDrugCategory(cmbCategory.getValue());
					drug.setDrugCommonName(txtComName.getText());
					drug.setDrugMedicalName(txtMedName.getText());
					drug.setDrugUPC(txtUniverCode.getText());
					drug.setDrugDosage(txtDosage.getText());
					drug.setDrugQtyPerPack(Integer.valueOf(txtQtyPerPack.getText()));
					drug.setDrugControlDrug(cmbControlDrug.getValue());
					drug.setDrugDescription(txtDescription.getText());
					drug.setDrugManufacturer(txtManufacturer.getText());
					drug.setDrugEmployee(LoginScreenController.emp);
					dataBase.update(drug);
					if(noti != null) {
						noti.showNotification("Drug Updated Successfully");
					}
					Util.hideWindow(event);
				}else {
					Dialog.error("Duplicate Entry for Universal Product Code");
				}
			}else {
				Dialog.error("Duplicate Entry for Drug Common Name");
			}
		}
	}
	
    @FXML
    void btnCrossAction(ActionEvent event) {
    	rootDrugUpdate.getScene().getWindow().hide();
    }
	public void setDrug(Drug d) {
		drug = d;

		cmbCategory.setValue(drug.getDrugCategory());
		txtComName.setText(drug.getDrugCommonName());
		txtMedName.setText(drug.getDrugMedicalName());
		txtUniverCode.setText(drug.getDrugUPC());
		txtDosage.setText(drug.getDrugDosage());
		txtQtyPerPack.setText(drug.getDrugQtyPerPack().toString());
		cmbControlDrug.setValue(drug.getDrugControlDrug());
		txtDescription.setText(drug.getDrugDescription());
		txtManufacturer.setText(drug.getDrugManufacturer());
	}

	public void sendReferance(DataBase dataBase, Notification noti) {
		this.dataBase = dataBase;
		this.noti = noti;
	}

	private void categorySelect() {
		cmbCategory.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("Pharma")) {
					txtMedName.setDisable(false);
				}
				else {
					txtMedName.setDisable(true);
				}
			}
		});
	}

	private void generateUPC() {
		txtUniverCode.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				Util.generateUPC(event, txtUniverCode);
			}
		});
	}


}
