package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import interfaces.Notification;
import interfaces.ProductInterface;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Drug;
import utils.Util;

public class ProductsController implements Initializable{

	@FXML
	private HBox barColor;

	@FXML
	private JFXTextField txtSearch;

	@FXML
	private JFXButton btnAdd;

	@FXML
	private TableView<Drug> tblPurchaseProduct;

	@FXML
	private TableColumn<Drug, String> colUpc;

	@FXML
	private TableColumn<Drug, String> colCommonName;

	@FXML
	private TableColumn<Drug, String> colMedicine;

	@FXML
	private TableColumn<Drug, String> colCatagory;

	@FXML
	private TableColumn<Drug, Drug> colAction;
	

    @FXML
    private BorderPane mainBorder;

	private ProductInterface productCallback;
	private DataBase dataBase;
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

	@FXML
	public void btnAddAction(ActionEvent event) {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DrugRegistration.fxml"));
			Parent root = fxmlLoader.load();
			DrugRegistrationController drc = (fxmlLoader).getController();
			drc.sendReferance(dataBase,new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(barColor, msg);
				}
			});
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTableCell() {
		colUpc.setCellValueFactory(data -> data.getValue().drugUPCProperty());
		colCommonName.setCellValueFactory(data -> data.getValue().drugCommonNameProperty());
		colMedicine.setCellValueFactory(data -> data.getValue().drugMedicalNameProperty());
		colCatagory.setCellValueFactory(data -> data.getValue().drugCategoryProperty());	    	
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private <T> void filterSearch() {
		@SuppressWarnings("unchecked")
		ObservableList<Drug> list = (ObservableList<Drug>) ((ObservableList<T>)dataBase.getList());
		FilteredList<Drug> filteredList = new FilteredList<>(list, p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(drug -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return drug.getDrugCommonName().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugMedicalName().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugCategory().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugUPC().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Drug> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblPurchaseProduct.comparatorProperty());
		tblPurchaseProduct.setItems(sortedList);
	}

	private void setAction() {
		colAction.setCellFactory(param -> new TableCell<Drug, Drug>() {
			@Override
			protected void updateItem(Drug drug, boolean empty) {
				super.updateItem(drug, empty);

				if (drug == null) {
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
					productCallback.setDrug(drug);
					Util.hideWindow(event);
				});
			}
		});
	}

	private void mouseClick() {
		tblPurchaseProduct.setRowFactory(tr -> {
			TableRow<Drug> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblPurchaseProduct.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblPurchaseProduct.getItems().size() && tblPurchaseProduct.getSelectionModel().isSelected(index)  ) {
								tblPurchaseProduct.getSelectionModel().clearSelection();
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
		tblPurchaseProduct.setOnKeyPressed(event ->{
			if(enter.match(event)){
				int index = tblPurchaseProduct.getSelectionModel().getSelectedIndex();
				productCallback.setDrug(tblPurchaseProduct.getItems().get(index));
				Util.hideWindow(event);
			}
		});
	}

	public void sendReference(DataBase dataBase,boolean btn, ProductInterface callBack) {
		this.dataBase = dataBase;
		this.productCallback = callBack;
		btnAdd.setVisible(btn);
		filterSearch();
	}

	
	private void shortCutKeys() {
		txtSearch.getParent().setOnKeyPressed(event ->{
			if(escape.match(event)) {
				Util.hideWindow(event);
			}
		});
		
		txtSearch.setOnKeyPressed(event ->{
			if(tab.match(event)) {
				tblPurchaseProduct.requestFocus();
				tblPurchaseProduct.getSelectionModel().select(0);
				tblPurchaseProduct.getFocusModel().focus(0);
				btnAdd.setFocusTraversable(false);
			}
		});
	}
	
	@FXML
    void btnCrossAction(ActionEvent event) {
    	mainBorder.getScene().getWindow().hide();
    }


}
