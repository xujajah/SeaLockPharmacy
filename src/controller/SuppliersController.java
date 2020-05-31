package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import interfaces.Notification;
import interfaces.SupplierInterface;
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
import model.Supplier;
import utils.Util;

public class SuppliersController implements Initializable{

	@FXML
	private HBox barColor;

    @FXML
    private BorderPane mainBorder;
    
	@FXML
	private JFXTextField txtSearch;

	@FXML
	private JFXButton btnAdd;

	@FXML
	private TableView<Supplier> tblPurchaseSupplier;

	@FXML
	private TableColumn<Supplier, String> colName;

	@FXML
	private TableColumn<Supplier, String> colPhone;

	@FXML
	private TableColumn<Supplier, String> colAddress;

	@FXML
	private TableColumn<Supplier, String>colEmail;

	@FXML
	private TableColumn<Supplier, Supplier> colAction;

	SupplierInterface supplierCallback;
	private DataBase dataBase;
	private KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
	private KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE);
	private KeyCombination tab = new KeyCodeCombination(KeyCode.TAB);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTableCell();
		mouseClick();
		setAction();
		enterProduct();
		shortCutKeys();
	}


	@FXML
	public void btnAddAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SupplierRegistration.fxml"));
			Parent root = fxmlLoader.load();
			SupplierRegistrationController src = (fxmlLoader).getController();
			src.sendReferance(dataBase, new Notification() {

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
		colName.setCellValueFactory(data -> data.getValue().suppNameProperty());
		colPhone.setCellValueFactory(data -> data.getValue().suppPhoneProperty());
		colAddress.setCellValueFactory(data -> data.getValue().suppAddressProperty());
		colEmail.setCellValueFactory(data -> data.getValue().suppEmailProperty());	    	
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private <T> void filterSearch() {
		@SuppressWarnings("unchecked")
		ObservableList<Supplier> list = (ObservableList<Supplier>) ((ObservableList<T>)dataBase.getList());
		FilteredList<Supplier> filteredList = new FilteredList<>(list, p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(supplier -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return supplier.getSuppName().toLowerCase().contains(lowerCaseFilter)
						|| supplier.getSuppPhone().toLowerCase().contains(lowerCaseFilter)
						|| supplier.getSuppAddress().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Supplier> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblPurchaseSupplier.comparatorProperty());
		tblPurchaseSupplier.setItems(sortedList);
	}

	private void setAction() {
		colAction.setCellFactory(param -> new TableCell<Supplier, Supplier>() {
			@Override
			protected void updateItem(Supplier supplier, boolean empty) {
				super.updateItem(supplier, empty);

				if (supplier == null) {
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
					supplierCallback.setSupplier(supplier);
					Util.hideWindow(event);
				});
			}
		});
	}

	private void mouseClick() {
		tblPurchaseSupplier.setRowFactory(tr -> {
			TableRow<Supplier> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblPurchaseSupplier.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblPurchaseSupplier.getItems().size() && tblPurchaseSupplier.getSelectionModel().isSelected(index)  ) {
								tblPurchaseSupplier.getSelectionModel().clearSelection();
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
		tblPurchaseSupplier.setOnKeyPressed(event ->{
			if(enter.match(event)){
				int index = tblPurchaseSupplier.getSelectionModel().getSelectedIndex();
				supplierCallback.setSupplier(tblPurchaseSupplier.getItems().get(index));
				Util.hideWindow(event);
			}
		});
	}

	public void sendReference(DataBase dataBase,boolean btn, SupplierInterface callBack) {
		this.dataBase = dataBase;
		this.supplierCallback = callBack;
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
				tblPurchaseSupplier.requestFocus();
				tblPurchaseSupplier.getSelectionModel().select(0);
				tblPurchaseSupplier.getFocusModel().focus(0);
				btnAdd.setFocusTraversable(false);
			}
		});
	}
	
	@FXML
    void btnCrossAction(ActionEvent event) {
    	mainBorder.getScene().getWindow().hide();
    }

}
