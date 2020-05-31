package controller;

import javafx.fxml.FXML;

import javafx.scene.layout.HBox;

import javafx.scene.control.TabPane;

import javafx.scene.layout.BorderPane;

public class InventoryController {
	@FXML
	private BorderPane rootInventoy;
	@FXML
	private HBox barColor;
	@FXML
	private TabPane tabPane;
	
	@FXML
	private DrugViewController drugViewController;
	
	@FXML
	private StockController stockController;

}