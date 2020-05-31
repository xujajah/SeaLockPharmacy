package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import database.PurchaseDetailDB;
import interfaces.Notification;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.PurchaseDetail;
import utils.SerialCell;
import utils.Util;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class ExpiryViewController implements Initializable{

	@FXML
	private BorderPane rootExpiryView;

	@FXML
	private TableView<PurchaseDetail> tblViewExpiry;

	@FXML
	private TableColumn<PurchaseDetail, Number> colSerial;

	@FXML
	private TableColumn<PurchaseDetail, String> colMedicineName;

	@FXML
	private TableColumn<PurchaseDetail, String> colBatchNo;

	@FXML
	private TableColumn<PurchaseDetail, Date> colExpiryDate;

	@FXML
	private TableColumn<PurchaseDetail, Number> colQty;

	@FXML
	private TableColumn<PurchaseDetail, String> colSupplier;

	@FXML
	private TableColumn<PurchaseDetail, String> colBill;

	@FXML
	private TableColumn<PurchaseDetail, Date> colPurchaseDate;

	@FXML
	private TableColumn<PurchaseDetail, PurchaseDetail> colAction;

	@FXML
	private TextField txtSearch;

	private PurchaseDetailDB dbPurchaseDetail;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbPurchaseDetail = new PurchaseDetailDB();
		setTableCell();
		setAction();
		mouseClick();
		filterSearch();
	}

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<PurchaseDetail,Number>, TableCell<PurchaseDetail,Number>>() {

			@Override
			public TableCell<PurchaseDetail, Number> call(TableColumn<PurchaseDetail, Number> param) {
				return new SerialCell<>();
			}
		});
		colMedicineName.setCellValueFactory(data -> data.getValue().purchaseDrugProperty().getValue().drugCommonNameProperty());
		colBatchNo.setCellValueFactory(data -> data.getValue().batchNoProperty());
		colExpiryDate.setCellValueFactory(data -> data.getValue().expiryDateProperty());
		colQty.setCellValueFactory(data -> data.getValue().qtyPerUnitProperty());
		colSupplier.setCellValueFactory(data -> data.getValue().purchaseProperty().getValue().purchaseSupplierProperty().getValue().suppNameProperty());
		colBill.setCellValueFactory(data -> data.getValue().purchaseProperty().getValue().purchaseSupplierBillProperty());
		colPurchaseDate.setCellValueFactory(data -> data.getValue().purchaseProperty().getValue().purchaseDateProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private void setAction() {
		colAction.setCellFactory(param -> new TableCell<PurchaseDetail, PurchaseDetail>() {
			@Override
			protected void updateItem(PurchaseDetail purchaseDetail, boolean empty) {
				super.updateItem(purchaseDetail, empty);

				if (purchaseDetail == null) {
					setGraphic(null);
					return;
				}
				HBox hb;
				Button deleteButton;
				hb = new HBox();

				hb.setPadding(new Insets(0, 3, 0, 3));
				//15, 12, 15, 12
				//				hb.setSpacing(10);
				hb.setAlignment(Pos.CENTER);
				deleteButton = new Button();

				Image image = new Image("/media/delete.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);


				deleteButton.setGraphic(imageView);
				deleteButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(deleteButton);
				setGraphic(hb);
				deleteButton.setOnAction(event -> {      	
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ExpiryPopUp.fxml"));
						Parent root = fxmlLoader.load();
						ExpiryPopUpController epc = (fxmlLoader).getController();
						epc.sendReferance(purchaseDetail, dbPurchaseDetail, new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootExpiryView, msg);
							}
						});
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.initStyle(StageStyle.UNDECORATED);

						stage.show();


					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		});
	}

	private void mouseClick() {
		tblViewExpiry.setRowFactory(tr -> {
			TableRow<PurchaseDetail> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblViewExpiry.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblViewExpiry.getItems().size() && tblViewExpiry.getSelectionModel().isSelected(index)  ) {
								tblViewExpiry.getSelectionModel().clearSelection();
								event.consume();  
							}  
						}  
					});
				}
			});

			return row;
		});
	}

	private void filterSearch() {
		FilteredList<PurchaseDetail> filteredList = new FilteredList<>(dbPurchaseDetail.expiryReterive(), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(purchaseDetail -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return purchaseDetail.getPurchaseDrug().getDrugCommonName().toLowerCase().contains(lowerCaseFilter)
						|| purchaseDetail.getPurchase().getPurchaseSupplier().getSuppName().toLowerCase().contains(lowerCaseFilter)
						|| purchaseDetail.getPurchase().getPurchaseSupplierBill().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<PurchaseDetail> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblViewExpiry.comparatorProperty());
		tblViewExpiry.setItems(sortedList);
	}

}
