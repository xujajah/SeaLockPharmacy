package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.RegionDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.Province;
import model.Region;
import utils.Util;

public class RegionController implements Initializable{

    @FXML
    private BorderPane mainBorder;

    @FXML
    private JFXTextField txtRegion;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXButton btnCross;
    
    private RegionDB regionDB;
    private Province province;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	Util.checkName(txtRegion);
		
	}

    @FXML
    public void btnCrossAction(ActionEvent event) {
    	Util.hideWindow(event);
    }

    @FXML
    public void btnRegionCancelAction(ActionEvent event) {
    	Util.hideWindow(event);
    }

    @FXML
    public void btnRegionSaveAction(ActionEvent event) {
    	if(Util.isValid(txtProvince) && Util.isValid(txtRegion)) {
    		Region region = new Region();
    		region.setRegionName(txtRegion.getText());
    		region.setRegionProvince(province);
    		regionDB.create(region);
    		Util.hideWindow(event);
    		
    	}
    }
    
    public void sendReference(RegionDB regionDB,Province province) {
		this.regionDB = regionDB;
		this.province = province;
		txtProvince.setText(this.province.toString());
	}

}
