package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import database.PurchaseDetailDB;
import interfaces.ProductInterface;
import interfaces.SupplierInterface;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import model.Drug;
import model.Purchase;
import model.PurchaseDetail;
import model.Supplier;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class PurchaseController implements Initializable{

	@FXML
	private HBox barColor;

	@FXML
	private JFXButton btnClear;

	@FXML
	private JFXTextField txtSupplier;

	@FXML
	private JFXButton btnSearchSupplier;

	@FXML
	private JFXDatePicker DatePurchase;

	@FXML
	private JFXTextField txtSupplierBillNo;

	@FXML
	private JFXTextField txtProductName;

	@FXML
	private JFXButton btnSearchProduct;

	@FXML
	private JFXTextField txtQuantityPerUnit;

	@FXML
	private JFXTextField txtTradePrice;

	@FXML
	private JFXTextField txtRetailPrice;

	@FXML
	private JFXTextField txtBatchNo;

	@FXML
	private JFXDatePicker dateExpiry;

	@FXML
	private JFXButton btnAdd;

	@FXML
	private TableView<PurchaseDetail> tblPurchaseItem;

	@FXML
	private TableColumn<PurchaseDetail, Number> colSno;

	@FXML
	private TableColumn<PurchaseDetail, String> colProductName;

	@FXML
	private TableColumn<PurchaseDetail, Number> colQuantity;

	@FXML
	private TableColumn<PurchaseDetail, Number> colTradePrice;

	@FXML
	private TableColumn<PurchaseDetail, Number> colRetailPrice;

	@FXML
	private TableColumn<PurchaseDetail, String> colBatchNo;

	@FXML
	private TableColumn<PurchaseDetail, Date> colExpiry;

	@FXML
	private TableColumn<PurchaseDetail, PurchaseDetail> colAction;

	@FXML
	private JFXButton btnAddsupply;

	@FXML
	private JFXButton btnCancelsupply;

	private DataBase dbDrug;
	private DataBase dbSupplier;
	private PurchaseDetailDB dbPurchaseDetail;

	private ObservableList<Drug> drugsList;
	private ObservableList<Supplier> suppliersList;

	private Drug drug;
	private Supplier supplier;

	private ObservableList<PurchaseDetail> purchaseDetailList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		purchaseDetailList = FXCollections.observableArrayList();
		dbPurchaseDetail = new PurchaseDetailDB();
		dbDrug = new DataBase();
		dbSupplier = new DataBase();
		drugsList = dbDrug.retrieve(Drug.class,"drugActive",true);
		suppliersList = dbSupplier.retrieve(Supplier.class,"suppActive",true);
		TextFields.bindAutoCompletion(txtProductName, drugsList);
		TextFields.bindAutoCompletion(txtSupplier, suppliersList);
		DatePurchase.setValue(LocalDate.now());

		Util.checkPositiveNumber(txtQuantityPerUnit);
		Util.checkDecimalNumber(txtTradePrice);
		Util.checkDecimalNumber(txtRetailPrice);
		dateExpiry.setPromptText("mm/dd/yy");
		DatePurchase.setPromptText("mm/dd/yy");

		onSelectSupplier();
		onSelectProduct();

		setTableCell();
		setAction();
		mouseClick();

	}

	@FXML
	public void btnAddAction(ActionEvent event) {
		if(Util.isValid(txtProductName) && Util.isValid(txtQuantityPerUnit)
				&& Util.isValid(txtTradePrice) && Util.isValid(txtRetailPrice)) {
			PurchaseDetail purchaseDetail = new PurchaseDetail();

			if(!txtBatchNo.isDisable()&& !dateExpiry.isDisable()) {
				if(Util.isValid(txtBatchNo) && Util.isValid(dateExpiry)) {
					purchaseDetail.setPurchaseDrug(drug);
					purchaseDetail.setQtyPerUnit(Integer.valueOf(txtQuantityPerUnit.getText()));
					purchaseDetail.setTradePrice(Double.valueOf(txtTradePrice.getText()));
					purchaseDetail.setRetailPrice(Double.valueOf(txtRetailPrice.getText()));
					purchaseDetail.setBatchNo(txtBatchNo.getText());
					purchaseDetail.setExpiryDate(Util.localDateToJavaSqlDate(dateExpiry.getValue()));
					purchaseDetail.setExpiryActive(true);

					for(PurchaseDetail p : purchaseDetailList) {
						if(p.getPurchaseDrug().equals(purchaseDetail.getPurchaseDrug())
								&& p.getBatchNo().equals(purchaseDetail.getBatchNo()) ) {
							p.setQtyPerUnit(p.getQtyPerUnit() + purchaseDetail.getQtyPerUnit());
							p.setTradePrice(purchaseDetail.getTradePrice());
							p.setRetailPrice(purchaseDetail.getRetailPrice());
							p.setExpiryDate(purchaseDetail.getExpiryDate());
							clearDetail();
							return;
						}
					}

					purchaseDetailList.add(purchaseDetail);
					clearDetail();
				}
			}
			else {
				purchaseDetail.setPurchaseDrug(drug);
				purchaseDetail.setQtyPerUnit(Integer.valueOf(txtQuantityPerUnit.getText()));
				purchaseDetail.setTradePrice(Double.valueOf(txtTradePrice.getText()));
				purchaseDetail.setRetailPrice(Double.valueOf(txtRetailPrice.getText()));
				purchaseDetail.setBatchNo(null);
				purchaseDetail.setExpiryDate(null);
				purchaseDetail.setExpiryActive(false);

				for(PurchaseDetail p : purchaseDetailList) {
					if(p.getPurchaseDrug().equals(purchaseDetail.getPurchaseDrug()) ) {
						p.setQtyPerUnit(p.getQtyPerUnit() + purchaseDetail.getQtyPerUnit());
						p.setTradePrice(purchaseDetail.getTradePrice());
						p.setRetailPrice(purchaseDetail.getRetailPrice());
						clearDetail();
						return;
					}
				}
				purchaseDetailList.add(purchaseDetail);
				clearDetail();
			}
		}
	}

	@FXML
	public void btnClearAction(ActionEvent event) {
		clearDetail();
	}

	@FXML
	public void btnSearchProductAction(ActionEvent event) {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Products.fxml"));
			Parent root = fxmlLoader.load();
			ProductsController pc = (fxmlLoader).getController();
			pc.sendReference(dbDrug, true,new ProductInterface() {

				@Override
				public void setDrug(Drug drug) {
					setDrugObject(drug);
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

	@FXML
	public void btnSearchSupplierAction(ActionEvent event){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Suppliers.fxml"));
			Parent root = fxmlLoader.load();
			SuppliersController sc = (fxmlLoader).getController();
			sc.sendReference(dbSupplier, true, new SupplierInterface() {

				@Override
				public void setSupplier(Supplier supplier) {
					setSupplierObject(supplier);
				}
			});
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();

		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void btnAddsupplyAction(ActionEvent event) {
		if(Util.isValid(txtSupplier) && Util.isValid(DatePurchase)) {
			if(!purchaseDetailList.isEmpty()) {
				Purchase purchase = new Purchase();
				purchase.setPurchaseSupplier(supplier);
				purchase.setPurchaseEmployee(LoginScreenController.emp);
				purchase.setPurchaseDate(Util.localDateToJavaSqlDate(DatePurchase.getValue()));
				purchase.setPurchaseSupplierBill(txtSupplierBillNo.getText());
				dbPurchaseDetail.createPurcahse(purchase, purchaseDetailList);
				Util.showNotification(barColor, "Supply Added");
				clearAll();
			}
			else {
				Dialog.error("Firstly Select Products to Add Supply");
			}
		}
	}

	@FXML
	public void btnCancelsupplyAction(ActionEvent event) {
		clearAll();
	}

	private void setDrugObject(Drug drug) {
		this.drug = drug;
		txtProductName.setText(this.drug.toString());
		if(this.drug.getDrugCategory().equals("General") || this.drug.getDrugCategory().equals("Others")) {
			dateExpiry.setDisable(true);
			txtBatchNo.setDisable(true);
		}
		else {
			dateExpiry.setDisable(false);
			txtBatchNo.setDisable(false);
		}
		txtQuantityPerUnit.requestFocus();
	}

	private void setSupplierObject(Supplier supplier) {
		this.supplier = supplier;
		txtSupplier.setText(this.supplier.toString());
		DatePurchase.requestFocus();
	}

	private void onSelectSupplier() {
		txtSupplier.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtSupplier.getText().isEmpty()) {
					for(Supplier s : suppliersList) {
						if(s.getSuppName().equals(txtSupplier.getText())) {
							supplier = s;
							return;
						}
					}
					txtSupplier.clear();
					txtSupplier.requestFocus();
					Dialog.error("This Supplier is Not Registered");
					return;
				}
			}
		});
	}

	private void onSelectProduct() {
		txtProductName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtProductName.getText().isEmpty()) {
					for(Drug d : drugsList) {
						if(d.getDrugCommonName().equals(txtProductName.getText())) {
							drug = d;
							if(drug.getDrugCategory().equals("General") || drug.getDrugCategory().equals("Others")) {
								dateExpiry.setDisable(true);
								txtBatchNo.setDisable(true);
							}
							else {
								dateExpiry.setDisable(false);
								txtBatchNo.setDisable(false);
							}
							return;
						}
					}
					txtProductName.clear();
					txtProductName.requestFocus();
					Dialog.error("This Product is Not Registered");
					return;
				}
			}
		});
	}

	private void setTableCell() {
		tblPurchaseItem.setEditable(true);
		colSno.setCellFactory(new Callback<TableColumn<PurchaseDetail,Number>, TableCell<PurchaseDetail,Number>>() {

			@Override
			public TableCell<PurchaseDetail, Number> call(TableColumn<PurchaseDetail, Number> param) {
				return new SerialCell<>();
			}
		});
		colProductName.setCellValueFactory(data -> data.getValue().purchaseDrugProperty().get().drugCommonNameProperty());
		colQuantity.setCellValueFactory(data -> data.getValue().qtyPerUnitProperty());
		colTradePrice.setCellValueFactory(data -> data.getValue().tradePriceProperty());
		colRetailPrice.setCellValueFactory(data -> data.getValue().retailPriceProperty());
		colBatchNo.setCellValueFactory(data -> data.getValue().batchNoProperty());
		colExpiry.setCellValueFactory(data -> data.getValue().expiryDateProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		colTradePrice.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		colRetailPrice.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

		tblPurchaseItem.setItems(purchaseDetailList);

	}

	private void mouseClick() {
		tblPurchaseItem.setRowFactory(tr -> {
			TableRow<PurchaseDetail> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblPurchaseItem.getSelectionModel().clearSelection();
				}
			});

			return row;
		});
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
				hb.setSpacing(10);
				deleteButton = new Button();

				Image image = new Image("/media/delete.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);
				deleteButton.setGraphic(imageView);
				deleteButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(deleteButton);
				hb.setAlignment(Pos.CENTER);
				setGraphic(hb);

				deleteButton.setOnAction(
						event -> {
							if(Dialog.confirmAlert(null, "Are you Sure! you want to delete")){
								purchaseDetailList.remove(purchaseDetail);
								tblPurchaseItem.requestFocus();
							}
						}
						);
			}
		});
	}

	private void clearDetail() {
		txtProductName.clear();
		txtQuantityPerUnit.clear();
		txtTradePrice.clear();
		txtRetailPrice.clear();
		txtBatchNo.clear();
		dateExpiry.getEditor().clear();
		txtProductName.requestFocus();
	}

	private void clearAll() {
		clearDetail();
		purchaseDetailList.clear();
		txtSupplier.clear();
		DatePurchase.setValue(LocalDate.now());
		txtSupplierBillNo.clear();
		txtSupplier.requestFocus();
	}

}
