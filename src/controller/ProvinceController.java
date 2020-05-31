package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.Province;
import utils.Util;

public class ProvinceController implements Initializable{

    @FXML
    private BorderPane mainBorder;

    @FXML
    private JFXButton btnCross;

    @FXML
    private JFXTextField txtProvince;
    
    private DataBase provinceDB;

    @FXML
    void btnCrossAction(ActionEvent event) {
    	Util.hideWindow(event);
    }

    @FXML
    void btnProvinceCancelAction(ActionEvent event) {
    	Util.hideWindow(event);
    }

    @FXML
    void btnProvinceSaveAction(ActionEvent event) {
    	if(Util.isValid(txtProvince)) {
    		Province province = new Province();
    		province.setProvinceName(txtProvince.getText());
    		provinceDB.create(province);
    		Util.hideWindow(event);
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Util.checkName(txtProvince);
	}
	
	public void sendReference(DataBase provinceDB) {
		this.provinceDB = provinceDB;
	}

}
