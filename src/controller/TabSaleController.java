package controller;


import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import database.PrescriptionDB;
import database.PrescriptionDetailDB;
import database.SaleDetailDB;
import database.StockDB;
import interfaces.CustomerInterface;
import interfaces.DoctorInterface;
import interfaces.Notification;
import interfaces.StockInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import model.Customer;
import model.Doctor;
import model.Prescription;
import model.PrescriptionDetail;
import model.Sale;
import model.SaleDetail;
import model.Stock;
import printing.POS;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabSaleController implements Initializable{

	@FXML
	private Label lblStock;

	@FXML
	private BorderPane borderPane;

	@FXML
	private JFXTextField txtPatientName;

	@FXML
	private JFXComboBox<Prescription> cmbViewPrescription;

	@FXML
	private JFXTextField txtDosageDays;

	@FXML
	private JFXTextField txtSearchMed;

	@FXML
	private JFXTextField txtDoctorName;

	@FXML
	private JFXTextField txtQuantity;

	@FXML
	private JFXTextField txtSearchPreOrder;

	@FXML
	private Label lblTotalItems;

	@FXML
	private Label lblAmountDue;

	@FXML
	private JFXTextField txtDiscount;

	@FXML
	private Label lblPayable;

	@FXML
	private JFXTextField txtReceive;

	@FXML
	private JFXCheckBox chkShippment;

	@FXML
	private Label lblBalance;

	@FXML
	private TableView<SaleDetail> tblSales;

	@FXML
	private TableColumn<SaleDetail, Number> colSalesNo;

	@FXML
	private TableColumn<SaleDetail, String> colSalesMedId;

	@FXML
	private TableColumn<SaleDetail, String> colSalesMedicineName;

	@FXML
	private TableColumn<SaleDetail, Number> colSalesQty;

	@FXML
	private TableColumn<SaleDetail, Number> colSalesPrice;

	@FXML
	private TableColumn<SaleDetail, Number> colSalesTotal;

	@FXML
	private TableColumn<SaleDetail, SaleDetail> colSalesAction;

	private StockDB dbStock;
	private DataBase dbCustomer;
	private DataBase dbDoctor;
	private PrescriptionDB dbPrescription;
	private PrescriptionDetailDB dbPrescriptionDetail;
	private ObservableList<Stock> stockList;
	private ObservableList<Customer> customersList;
	private ObservableList<Doctor> doctorsList;
	private ObservableList<Prescription> prescriptionList;
	private ObservableList<PrescriptionDetail> prescriptionDetailList;
	private ObservableList<SaleDetail> salesDetailList;
	private ObservableList<SaleDetail> printSalesDetailList;
	private int orderId=0;

	private Customer customer;
	private Doctor doctor;
	private Stock stockdrug;
	private Prescription prescription;
	private Sale sale = new Sale();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customer = (Customer) DBUtil.getObject(Customer.class, "custId", 1);
		txtPatientName.setText(customer.getCustName());
		txtSearchMed.requestFocus();
		salesDetailList = FXCollections.observableArrayList();
		printSalesDetailList = FXCollections.observableArrayList();
		prescriptionList = FXCollections.observableArrayList();
		prescriptionDetailList = FXCollections.observableArrayList();
		onSelectCustomer();
		onSelectDrug();
		onSelectDoctor();
		onEnterDiscount();
		onEnterCashReceived();
		setTableCell();
		prescriptionSelect();
		setAction();
		mouseClick();
		onSelectDays();
		Util.checkPositiveNumber(txtDosageDays);
		Util.checkPositiveNumber(txtQuantity);
		Util.checkDecimalNumber(txtDiscount);
		Util.checkDecimalNumber(txtReceive);
		onActionCmbPrescription();
		txtDosageDays.setText("1");
		txtQuantity.setText("1");
		txtDiscount.setText("0");
		txtReceive.setText("0");
		searchProduct();
		onSetQuantity();
		labelBinding();



	}

	@FXML
	public void btnAddPatientAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Customers.fxml"));
			Parent root = fxmlLoader.load();
			CustomersController cc = (fxmlLoader).getController();
			cc.sendReference(dbCustomer, true, new CustomerInterface() {

				@Override
				public void setCustomer(Customer customer) {
					setCustomerObject(customer);
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


	}

	@FXML
	void btnAddDoctorAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Doctors.fxml"));
			Parent root = fxmlLoader.load();
			DoctorsController dc = (fxmlLoader).getController();
			dc.sendReference(dbDoctor, true, new DoctorInterface() {

				@Override
				public void setDoctor(Doctor doctor) {
					setDoctorObject(doctor);
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
	}

	@FXML
	void btnPrescriptionAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrescriptionRegistration.fxml"));
			Parent root = fxmlLoader.load();
			@SuppressWarnings("rawtypes")
			PrescriptionRegistrationController prc = (fxmlLoader).getController();
			prc.sendReferance(dbCustomer,dbDoctor, dbStock, dbPrescriptionDetail, new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(borderPane, msg);
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
	}

	@FXML
	public void btnSearchAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/StockSearch.fxml"));
			Parent root = fxmlLoader.load();
			StockSearchController ssc = (fxmlLoader).getController();
			ssc.sendReference(stockList, new StockInterface() {

				@Override
				public void setStock(Stock stock) {
					setDrugObject(stock);
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
	}

	@FXML
	public void btnCancelAction(ActionEvent event) {
		cancelOrder();
	}

	@FXML
	public void btnPrintAction(ActionEvent event) {		
		if(!salesDetailList.isEmpty()) {
			if(checkControlDrug()) {
				if(customer.getCustId() != 1) {
					if(!txtDoctorName.getText().isEmpty()) {
						sale.setSaleCustomer(customer);
						sale.setSaleDoctor(doctor);
						sale.setSaleEmployee(LoginScreenController.emp);
						sale.setSaleDate(new Date());
						sale.setSaleTime(new Date());
						sale.setSalePrescription(prescription);
						sale.setTotalDiscount(Double.valueOf(txtDiscount.getText()));
						sale.setTotalPayable(Double.valueOf(lblPayable.getText()));
						sale.setTotalProfit(sale.getTotalPayable() - sale.getTotalTradePrice() - sale.getTotalDiscount());
						sale.setSaleActive(true);
						SaleDetailDB saleDetailDB = new SaleDetailDB();
						orderId = saleDetailDB.createSale(sale, salesDetailList);
						Util.showNotification(borderPane, "Sale Saved");
						POS.pageSetup(getPrintNode());
						cancelOrder();
					}else {
						Dialog.error("Please Select the Doctor");
					}
				}else {
					Dialog.error("Please Select the Customer");
				}

			}else {
				sale.setSaleCustomer(customer);
				sale.setSaleDoctor(doctor);
				sale.setSaleEmployee(LoginScreenController.emp);
				sale.setSaleDate(new Date());
				sale.setSaleTime(new Date());
				sale.setSalePrescription(prescription);
				sale.setTotalDiscount(Double.valueOf(txtDiscount.getText()));
				sale.setTotalPayable(Double.valueOf(lblPayable.getText()));
				sale.setTotalProfit(sale.getTotalRetailPrice() - sale.getTotalTradePrice() - sale.getTotalDiscount());
				sale.setSaleActive(true);
				SaleDetailDB saleDetailDB = new SaleDetailDB();
				orderId = saleDetailDB.createSale(sale, salesDetailList);
				Util.showNotification(borderPane, "Sale Saved");
				POS.pageSetup(getPrintNode());
				cancelOrder();
			}
			stockList.clear();
			stockList = dbStock.stockReterive();
		}else {
			Dialog.error("Enter Products First");
		}

	}


	private BorderPane getPrintNode()
	{
		BorderPane root = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/BillPrint.fxml"));
			root = (BorderPane) fxmlLoader.load();
			BillPrintController controller = (fxmlLoader).getController();
			if(doctor==null) {
				doctor = new Doctor();
			}
			printSalesDetailList.clear();
			for(SaleDetail sd : salesDetailList) {
				printSalesDetailList.add(sd);
			}
			controller.setData(printSalesDetailList,customer.getCustName(),doctor.getDocName(),sale.getSaleDate(),orderId,sale.getSaleEmployee().getEmpName(),
					Integer.valueOf(lblTotalItems.getText()), Double.valueOf(lblAmountDue.getText()), Double.valueOf(txtDiscount.getText()),
					Double.valueOf(lblPayable.getText()),Double.valueOf(txtReceive.getText()), Double.valueOf(lblBalance.getText())   );			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	@FXML
	public void btnSaveAction(ActionEvent event) {
		saveSale();
	}

	private void saveSale() {
		if(!salesDetailList.isEmpty()) {
			if(checkControlDrug()) {
				if(customer.getCustId() != 1) {
					if(!txtDoctorName.getText().isEmpty()) {
						sale.setSaleCustomer(customer);
						sale.setSaleDoctor(doctor);
						sale.setSaleEmployee(LoginScreenController.emp);
						sale.setSaleDate(new Date());
						sale.setSaleTime(new Date());
						sale.setSalePrescription(prescription);
						sale.setTotalDiscount(Double.valueOf(txtDiscount.getText()));
						sale.setTotalPayable(Double.valueOf(lblPayable.getText()));
						sale.setTotalProfit(sale.getTotalPayable() - sale.getTotalTradePrice() - sale.getTotalDiscount());
						sale.setSaleActive(true);
						SaleDetailDB saleDetailDB = new SaleDetailDB();
						orderId = saleDetailDB.createSale(sale, salesDetailList);
						Util.showNotification(borderPane, "Sale Saved");
						cancelOrder();
					}else {
						Dialog.error("Please Select the Doctor");
					}
				}else {
					Dialog.error("Please Select the Customer");
				}

			}else {
				sale.setSaleCustomer(customer);
				sale.setSaleDoctor(doctor);
				sale.setSaleEmployee(LoginScreenController.emp);
				sale.setSaleDate(new Date());
				sale.setSaleTime(new Date());
				sale.setSalePrescription(prescription);
				sale.setTotalDiscount(Double.valueOf(txtDiscount.getText()));
				sale.setTotalPayable(Double.valueOf(lblPayable.getText()));
				sale.setTotalProfit(sale.getTotalRetailPrice() - sale.getTotalTradePrice() - sale.getTotalDiscount());
				sale.setSaleActive(true);
				SaleDetailDB saleDetailDB = new SaleDetailDB();
				orderId = saleDetailDB.createSale(sale, salesDetailList);
				Util.showNotification(borderPane, "Sale Saved");
				cancelOrder();
			}
			stockList.clear();
			stockList = dbStock.stockReterive();
		}else {
			Dialog.error("Enter Products First");
		}
	}

	private boolean checkControlDrug(){
		for(SaleDetail sd : salesDetailList) {
			if(sd.getSaleStockDrug().getStockDrug().getDrugControlDrug().equals("Yes")) {
				return true;
			}
		}
		return false;
	}


	public void setData(StockDB dbStock,ObservableList<Stock> stockList,
			DataBase dbCustomer,ObservableList<Customer> customersList,
			DataBase dbDoctor,ObservableList<Doctor> doctorsList,
			PrescriptionDB dbPrescription, PrescriptionDetailDB dbPrescriptionDetail) 
	{
		this.dbStock = dbStock;
		this.stockList = stockList;
		this.dbCustomer = dbCustomer;
		this.customersList = customersList;
		this.dbDoctor = dbDoctor;
		this.doctorsList = doctorsList;
		this.dbPrescription = dbPrescription;
		this.dbPrescriptionDetail = dbPrescriptionDetail;

		TextFields.bindAutoCompletion(txtSearchMed, this.stockList);
		TextFields.bindAutoCompletion(txtPatientName, this.customersList);
		TextFields.bindAutoCompletion(txtDoctorName, this.doctorsList);

	}

	private void setDrugObject(Stock stockDrug) {
		this.stockdrug = stockDrug;
		txtSearchMed.setText(this.stockdrug.toString());
		lblStock.setText(this.stockdrug.getStockTotal().toString());
		txtQuantity.requestFocus();
	}

	private void setCustomerObject(Customer customer) {
		this.customer = customer;
		txtPatientName.setText(this.customer.toString());
		txtDoctorName.requestFocus();
	}

	private void setDoctorObject(Doctor doctor) {
		this.doctor = doctor;
		this.txtDoctorName.setText(this.doctor.toString());
		txtSearchMed.requestFocus();
	}

	private void onSelectDrug() {
		txtSearchMed.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtSearchMed.getText().isEmpty()) {
					for(Stock s : stockList) {
						if(s.getStockDrug().getDrugCommonName().equals(txtSearchMed.getText())
								|| s.getStockDrug().getDrugUPC().equals(txtSearchMed.getText()) ) 
						{
							stockdrug = s;
							lblStock.setText(stockdrug.getStockTotal().toString());
							return;
						}
					}
					txtSearchMed.clear();
					txtSearchMed.requestFocus();
					Dialog.error("This Product is Not Registered");
					return;
				}
			}
		});
	}

	private void onSelectDoctor() {
		txtDoctorName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtDoctorName.getText().isEmpty()) {
					for(Doctor d : doctorsList) {
						if(d.getDocName().equals(txtDoctorName.getText())) {
							doctor = d;
							return;
						}
					}

					txtDoctorName.clear();
					txtDoctorName.requestFocus();
					Dialog.error("This Doctor is Not Registered");
					return;
				}
			}
		});
	}

	private void onSelectCustomer() {
		txtPatientName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!txtPatientName.getText().isEmpty()) {
					for(Customer c : customersList) {
						if(c.toString().equals(txtPatientName.getText())) {
							customer = c;
							return;
						}
					}
					txtPatientName.clear();
					txtPatientName.requestFocus();
					Dialog.error("This Customer is Not Registered");
					return;
				}
			}
		});
	}

	private void onEnterDiscount() {
		txtDiscount.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(txtDiscount.getText().isEmpty()) {
					txtDiscount.setText("0");
				}
			}
		});
	}


	private void onEnterCashReceived() {
		txtReceive.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(txtReceive.textProperty().isBound()) {
					txtReceive.textProperty().unbind();
					txtReceive.setOnKeyReleased(event ->{
						if(!txtReceive.getText().isEmpty()) {
							lblBalance.setText(String.valueOf (Double.valueOf(txtReceive.getText()) - Double.valueOf(lblPayable.getText()) ) );
						}
					});
				}
				if(!newValue) {
					if(txtReceive.getText().isEmpty()) {
						txtReceive.textProperty().bind(lblPayable.textProperty());
						lblBalance.setText("0");
					}
				}
			}
		});
	}

	private void setTableCell() {
		tblSales.setEditable(true);
		colSalesNo.setCellFactory(new Callback<TableColumn<SaleDetail,Number>, TableCell<SaleDetail,Number>>() {

			@Override
			public TableCell<SaleDetail, Number> call(TableColumn<SaleDetail, Number> arg0) {
				return new SerialCell<>();
			}
		});
		colSalesMedId.setCellValueFactory(data -> data.getValue().saleStockDrugProperty().get().stockDrugProperty().get().drugUPCProperty());
		colSalesMedicineName.setCellValueFactory(data -> data.getValue().saleStockDrugProperty().get().stockDrugProperty().get().drugCommonNameProperty());	
		colSalesQty.setCellValueFactory(data -> data.getValue().saleQtyProperty());
		colSalesQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		colSalesPrice.setCellValueFactory(data -> data.getValue().retailPriceProperty());
		colSalesTotal.setCellValueFactory(data -> data.getValue() .totalRetailPriceProperty());
		colSalesAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		colSalesNo.setStyle("-fx-alignment: CENTER;");
		colSalesQty.setStyle("-fx-alignment: CENTER-RIGHT;");
		colSalesPrice.setStyle("-fx-alignment: CENTER-RIGHT;");
		colSalesTotal.setStyle("-fx-alignment: CENTER-RIGHT;");
		tblSales.setItems(salesDetailList);
		colSalesQty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SaleDetail,Number>>() {

			@Override
			public void handle(CellEditEvent<SaleDetail, Number> event) {
				SaleDetail item = (SaleDetail) event.getTableView().getItems().get(event.getTablePosition().getRow());
				item.setSaleQty((int) event.getNewValue().doubleValue());
				for(SaleDetail sd : salesDetailList) {
					if(sd.getSaleStockDrug().equals(item.getSaleStockDrug())) {
						sd.setSaleQty(item.getSaleQty());
					}
				}
				calculateTotal();

			}
		});

	}

	private void mouseClick() {
		tblSales.setRowFactory(tr -> {
			TableRow<SaleDetail> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblSales.getSelectionModel().clearSelection();
				}
			});

			return row;
		});
	}


	private void setAction() {

		colSalesAction.setCellFactory(param -> new TableCell<SaleDetail, SaleDetail>() {
			@Override
			protected void updateItem(SaleDetail saleDetail, boolean empty) {
				super.updateItem(saleDetail, empty);

				if (saleDetail == null) {
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
								salesDetailList.remove(saleDetail);
								calculateTotal();
								tblSales.requestFocus();
							}
						}
						);}
		});
	}

	private void onActionCmbPrescription (){
		cmbViewPrescription.addEventFilter(KeyEvent.KEY_PRESSED, event ->{
			if (event.getCode() == KeyCode.ENTER) {
				if(!txtPatientName.getText().isEmpty()) {
					prescriptionList = dbPrescription.customerPrescriptions(customer.getCustId());
					cmbViewPrescription.setItems(prescriptionList);
				}
			}
		});
	}

	private void prescriptionSelect() {

		cmbViewPrescription.valueProperty().addListener(new ChangeListener<Prescription>() {

			@Override
			public void changed(ObservableValue<? extends Prescription> observable, Prescription oldValue,
					Prescription newValue) {
				salesDetailList.clear();
				for(Prescription p : prescriptionList) {
					if(newValue.equals(p)) {
						prescription = p;
						doctor = prescription.getPrescriptionDoctor();
						customer = prescription.getPrescriptionCustomer();
						txtPatientName.setText(customer.getCustName());
						txtDoctorName.setText(doctor.getDocName());

						prescriptionDetailList = dbPrescriptionDetail.getPrescriptionDetail(prescription.getPrescriptionId());

						for(PrescriptionDetail pd : prescriptionDetailList) {
							SaleDetail sd = new SaleDetail();
							sd.setSaleStockDrug(pd.getPrescriptionDetailDrug());
							sd.setSaleQty((int) pd.getPrescriptionDetailDosage());
							sd.setRetailPrice(pd.getPrescriptionDetailDrug().getStockRetailPrice() /
									pd.getPrescriptionDetailDrug().getStockDrug().getDrugQtyPerPack());
							sd.setTradePrice(pd.getPrescriptionDetailDrug().getStockTradePrice() / 
									pd.getPrescriptionDetailDrug().getStockDrug().getDrugQtyPerPack());
							salesDetailList.add(sd);
						}
						break;
					}
				}				
				tblSales.setItems(salesDetailList);
				calculateTotal();
			}
		});
	}

	private void onSelectDays() {

		txtDosageDays.setOnAction(event -> {
			if(prescriptionDetailList!= null) {
				salesDetailList.clear();
				for(PrescriptionDetail pd : prescriptionDetailList) {
					SaleDetail sd = new SaleDetail();
					sd.setSaleStockDrug(pd.getPrescriptionDetailDrug());
					sd.setSaleQty((int) pd.getPrescriptionDetailDosage() * Integer.valueOf(txtDosageDays.getText()) );
					sd.setRetailPrice(pd.getPrescriptionDetailDrug().getStockRetailPrice() /
							pd.getPrescriptionDetailDrug().getStockDrug().getDrugQtyPerPack());
					sd.setTradePrice(pd.getPrescriptionDetailDrug().getStockTradePrice() / 
							pd.getPrescriptionDetailDrug().getStockDrug().getDrugQtyPerPack());
					salesDetailList.add(sd);
					calculateTotal();
				}
			}
		});
	}

	private void searchProduct() {
		txtSearchMed.setOnAction(event ->{
			if(!txtQuantity.getText().isEmpty()) {
				if(!txtSearchMed.getText().isEmpty()) {
					for(Stock s : stockList) {
						if(s.getStockDrug().getDrugCommonName().equals(txtSearchMed.getText())
								|| s.getStockDrug().getDrugUPC().equals(txtSearchMed.getText()) ) 
						{
							stockdrug = s;
							lblStock.setText("");
							SaleDetail saleDetail = new SaleDetail();
							saleDetail.setSaleStockDrug(stockdrug);
							saleDetail.setSaleQty(Integer.valueOf(txtQuantity.getText()));
							saleDetail.setRetailPrice(stockdrug.getStockRetailPrice() /
									stockdrug.getStockDrug().getDrugQtyPerPack());
							saleDetail.setTradePrice(stockdrug.getStockTradePrice() / 
									stockdrug.getStockDrug().getDrugQtyPerPack());
							for(SaleDetail sd : salesDetailList) {
								if(sd.getSaleStockDrug().getId() == saleDetail.getSaleStockDrug().getId()) {
									sd.setSaleQty(sd.getSaleQty() + saleDetail.getSaleQty());
									txtSearchMed.clear();
									txtQuantity.setText("1");
									txtSearchMed.requestFocus();
									calculateTotal();
									return;
								}
							}
							salesDetailList.add(saleDetail);
							txtSearchMed.clear();
							txtQuantity.setText("1");
							txtSearchMed.requestFocus();
							calculateTotal();
							return;
						}
					}
					txtSearchMed.clear();
					txtSearchMed.requestFocus();
					Dialog.error("This Product is Not Registered");
					return;
				}


			}else {
				txtQuantity.requestFocus();
			}
		});
	}

	private void onSetQuantity() {
		txtQuantity.setOnAction(event ->{
			if(!txtQuantity.getText().isEmpty()) {
				if(!txtSearchMed.getText().isEmpty()) {
					for(Stock s : stockList) {
						if(s.getStockDrug().getDrugCommonName().equals(txtSearchMed.getText())
								|| s.getStockDrug().getDrugUPC().equals(txtSearchMed.getText()) ) 
						{
							stockdrug = s;
							SaleDetail saleDetail = new SaleDetail();
							saleDetail.setSaleStockDrug(stockdrug);
							saleDetail.setSaleQty(Integer.valueOf(txtQuantity.getText()));
							saleDetail.setRetailPrice(stockdrug.getStockRetailPrice() /
									stockdrug.getStockDrug().getDrugQtyPerPack());
							saleDetail.setTradePrice(stockdrug.getStockTradePrice() / 
									stockdrug.getStockDrug().getDrugQtyPerPack());
							for(SaleDetail sd : salesDetailList) {
								if(sd.getSaleStockDrug().equals(saleDetail.getSaleStockDrug())) {
									sd.setSaleQty(sd.getSaleQty() + saleDetail.getSaleQty());
									txtSearchMed.clear();
									txtQuantity.setText("1");
									lblStock.setText("");
									txtSearchMed.requestFocus();
									calculateTotal();
									return;
								}
							}
							salesDetailList.add(saleDetail);
							txtSearchMed.clear();
							txtQuantity.setText("1");
							lblStock.setText("");
							txtSearchMed.requestFocus();
							calculateTotal();
							return;
						}
					}
					txtSearchMed.clear();
					txtSearchMed.requestFocus();
					Dialog.error("This Product is Not Registered");
					return;
				}


			}else {
				txtQuantity.requestFocus();
			}
		});
	}

	
	private void labelBinding() {
		lblTotalItems.textProperty().bind(Bindings.size(salesDetailList).asString());

		SimpleDoubleProperty due = new SimpleDoubleProperty();
		Bindings.bindBidirectional(
				lblAmountDue.textProperty(), due, new NumberStringConverter()
				);
		SimpleDoubleProperty discount = new SimpleDoubleProperty();
		Bindings.bindBidirectional(
				txtDiscount.textProperty(), discount, new NumberStringConverter()
				);
		NumberBinding payableBinding = Bindings.subtract(due, discount);
		lblPayable.textProperty().bind(payableBinding.asString());
		txtReceive.textProperty().bind(lblPayable.textProperty());
		calculateTotal();


	}


	private void calculateTotal() {
		DoubleBinding totalRetailPrice = Bindings.createDoubleBinding(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				return (double) Math.round(tblSales.getItems().stream().collect(Collectors.summingDouble(new ToDoubleFunction<SaleDetail>() {

					@Override
					public double applyAsDouble(SaleDetail value) {
						return value.getTotalRetailPrice();
					}
				})) );
			}
		}, tblSales.getItems());
		lblAmountDue.textProperty().bind(Bindings.format("%.2f", totalRetailPrice));
		sale.setTotalRetailPrice(totalRetailPrice.doubleValue());


		DoubleBinding totalTradePrice  = Bindings.createDoubleBinding(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				return tblSales.getItems().stream().collect(Collectors.summingDouble(new ToDoubleFunction<SaleDetail>() {

					@Override
					public double applyAsDouble(SaleDetail value) {
						// TODO Auto-generated method stub
						return value.getTotalTradePrice();
					}
				}));
			}
		}, tblSales.getItems());
		sale.setTotalTradePrice(totalTradePrice.doubleValue());
	}


	private void cancelOrder() {
		salesDetailList.clear();
		prescriptionList.clear();
		prescriptionDetailList.clear();
		customer = (Customer) DBUtil.getObject(Customer.class, "custId", 1);
		prescription = null;
		stockdrug = null;
		doctor = null;
		sale = null;
		sale = new Sale();
		txtPatientName.setText(customer.getCustName());
		txtDoctorName.clear();
		cmbViewPrescription.getEditor().clear();
		txtDosageDays.setText("1");
		txtSearchMed.clear();
		txtQuantity.setText("1");
		lblStock.setText("");
		txtSearchMed.requestFocus();
		txtDiscount.setText("0");
		lblBalance.setText("0");

	}
}
