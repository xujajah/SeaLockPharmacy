package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import database.DBUtil;
import database.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.PosPrinter;
import utils.Util;

public class PrinterSelectController implements Initializable{

    @FXML
    private BorderPane rootPrinterSelect;

    @FXML
    private HBox barColor;

    @FXML
    private JFXButton btnCross;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblDefault;

    @FXML
    private JFXComboBox<String> cmbPrinters;
    
    ObservableList<String> printerlist = FXCollections.observableArrayList();
    ObservableSet<Printer> printers;
    PosPrinter posPrinter;
    DataBase db;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	posPrinter =  new PosPrinter();
    	db = new DataBase();
    	posPrinter = (PosPrinter) DBUtil.getObject(PosPrinter.class, 1);
    	lblDefault.setText(posPrinter.getName());
    	printers = Printer.getAllPrinters();
    	for(Printer printer : printers) {
    		printerlist.add(printer.getName());
    	}
    	cmbPrinters.setItems(printerlist);
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
    	if(Util.isValid(cmbPrinters)) {
    		posPrinter.setName(cmbPrinters.getValue());
    		db.update(posPrinter);
    		Util.showNotification(rootPrinterSelect, "Printer Selected Successfully");
    		lblDefault.setText(posPrinter.getName());
    	}

    }

}
