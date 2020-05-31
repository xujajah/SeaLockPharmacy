package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.StockDB;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Stock;
import utils.SerialCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class StockController implements Initializable{
	@FXML
	private BorderPane rootStock;
	@FXML
	private TableView<Stock> tblStock;
	@FXML
	private TableColumn<Stock,Number> colSerial;
	@FXML
	private TableColumn<Stock,String> colUpc;
	@FXML
	private TableColumn<Stock,String> colComName;
	@FXML
	private TableColumn<Stock,String> colMedName;
	@FXML
	private TableColumn<Stock,Number> colQtyPerPack;
	@FXML
	private TableColumn<Stock,Number> colStockInUnit;
	@FXML
	private TableColumn<Stock,Number> colTradePrice;
	@FXML
	private TableColumn<Stock,Number> colRetailPrice;
	@FXML
	private TableColumn<Stock,Number> colUnitPrice;
	@FXML
	private TextField txtSearch;
	
	private StockDB dbStock;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbStock = new StockDB();
		setTableCell();
		filterSearch();
		mouseClick();
	}

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<Stock,Number>, TableCell<Stock,Number>>() {

			@Override
			public TableCell<Stock, Number> call(TableColumn<Stock, Number> param) {
				return new SerialCell<>();
			}
		});
		colUpc.setCellValueFactory(data -> data.getValue().stockDrugProperty().getValue().drugUPCProperty());
		colComName.setCellValueFactory(data -> data.getValue().stockDrugProperty().getValue().drugCommonNameProperty());
		colMedName.setCellValueFactory(data -> data.getValue().stockDrugProperty().getValue().drugMedicalNameProperty());
		colQtyPerPack.setCellValueFactory(data -> data.getValue().stockDrugProperty().getValue().drugQtyPerPackProperty());
		colStockInUnit.setCellValueFactory(data -> data.getValue().stockTotalProperty());
		colTradePrice.setCellValueFactory(data -> data.getValue().stockTradePriceProperty());
		colRetailPrice.setCellValueFactory(data -> data.getValue().stockRetailPriceProperty());
		colUnitPrice.setCellValueFactory(data -> data.getValue().stockUnitPriceProperty());
	}
	
	private void filterSearch() {
		FilteredList<Stock> filteredList = new FilteredList<>(dbStock.stockReterive(), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(stock -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return stock.getStockDrug().getDrugCommonName().toLowerCase().contains(lowerCaseFilter)
						|| stock.getStockDrug().getDrugMedicalName().toLowerCase().contains(lowerCaseFilter)
						|| stock.getStockDrug().getDrugUPC().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Stock> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblStock.comparatorProperty());
		tblStock.setItems(sortedList);
	}

	private void mouseClick() {
		tblStock.setRowFactory(tr -> {
			TableRow<Stock> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblStock.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblStock.getItems().size() && tblStock.getSelectionModel().isSelected(index)  ) {
								tblStock.getSelectionModel().clearSelection();
								event.consume();  
							}  
						}  
					});
				}
			});

			return row;
		});
	}

}
