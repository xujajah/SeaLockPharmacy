package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import interfaces.StockInterface;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Stock;
import utils.SerialCell;
import utils.Util;

public class StockSearchController implements Initializable {
	
	private Scene scene;
	
    @FXML
    private BorderPane BPSaleSearch;
    @FXML
    private JFXTextField txtSearchMedicine;

    @FXML
    private TableView<Stock> tblSaleSearch;
    
    @FXML
    private TableColumn<Stock, Number> colSerial;

    @FXML
    private TableColumn<Stock, String>colUPC;

    @FXML
    private TableColumn<Stock, String> colCommonName;

    @FXML
    private TableColumn<Stock, String>colMedicineName;

    @FXML
    private TableColumn<Stock, Number>colQtyPerPack;

    @FXML
    private TableColumn<Stock, Number> ColStock;

    @FXML
    private TableColumn<Stock, Stock> ColAction;
    
    private StockInterface stockCallback;
    private ObservableList<Stock> stockList;
    private KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
	private KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE);
	private KeyCombination tab = new KeyCodeCombination(KeyCode.TAB);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setTableCell();
		mouseClick();
		setAction();
		enterProduct();
		shortCutKeys();
		
	}
	
	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<Stock,Number>, TableCell<Stock,Number>>() {
			
			@Override
			public TableCell<Stock, Number> call(TableColumn<Stock, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colUPC.setCellValueFactory(data -> data.getValue().getStockDrug().drugUPCProperty());
		colCommonName.setCellValueFactory(data -> data.getValue().getStockDrug().drugCommonNameProperty());
		colMedicineName.setCellValueFactory(data -> data.getValue().getStockDrug().drugMedicalNameProperty());
		colQtyPerPack.setCellValueFactory(data -> data.getValue().getStockDrug().drugQtyPerPackProperty());
		ColStock.setCellValueFactory(data -> data.getValue().stockTotalProperty());
		ColAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
	}
	
	private void filterSearch() {
		FilteredList<Stock> filteredList = new FilteredList<>(stockList, p -> true);
		txtSearchMedicine.textProperty().addListener((observable,oldValue,newValue) -> {
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
		sortedList.comparatorProperty().bind(tblSaleSearch.comparatorProperty());
		tblSaleSearch.setItems(sortedList);
	}

	private void setAction() {
		ColAction.setCellFactory(param -> new TableCell<Stock, Stock>() {
			@Override
			protected void updateItem(Stock stock, boolean empty) {
				super.updateItem(stock, empty);

				if (stock == null) {
					setGraphic(null);
					return;
				}
				HBox hb;
				Button selectButton;
				hb = new HBox();

				hb.setPadding(new Insets(0, 3, 0, 3));
				//15, 12, 15, 12
				hb.setSpacing(10);
				selectButton = new Button();

				Image image = new Image("/media/select.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);

				selectButton.setGraphic(imageView);
				selectButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(selectButton);
				setGraphic(hb);
				selectButton.setOnAction(event -> {
					stockCallback.setStock(stock);
					Util.hideWindow(event);
				});
			}
		});
	}

	private void mouseClick() {
		tblSaleSearch.setRowFactory(tr -> {
			TableRow<Stock> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblSaleSearch.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblSaleSearch.getItems().size() && tblSaleSearch.getSelectionModel().isSelected(index)  ) {
								tblSaleSearch.getSelectionModel().clearSelection();
								event.consume();  
							}  
						}  
					});
				}
			});

			return row;
		});
	}

	public void enterProduct(){
		tblSaleSearch.setOnKeyPressed(event ->{
			if(enter.match(event)){
				int index = tblSaleSearch.getSelectionModel().getSelectedIndex();
				stockCallback.setStock(tblSaleSearch.getItems().get(index));
				Util.hideWindow(event);
			}
		});
	}

	public void sendReference(ObservableList<Stock> stockList, StockInterface callBack) {
		this.stockList = stockList;
		this.stockCallback = callBack;
		filterSearch();
	}

	
	private void shortCutKeys() {
		
		txtSearchMedicine.setOnKeyPressed(event ->{
			if(tab.match(event)) {
				tblSaleSearch.requestFocus();
				tblSaleSearch.getSelectionModel().select(0);
				tblSaleSearch.getFocusModel().focus(0);
			}
		});
	}
	
	public void setScene(Scene scene) { 
		this.scene = scene; 
		this.scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(escape.match(event)) {
					scene.getWindow().hide();				
				}
			}
			
		});
	}
	
	@FXML
    void btnCrossAction(ActionEvent event) {
		BPSaleSearch.getScene().getWindow().hide();
    }


}
