package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.SaleDetailDB;
import database.SaleReturnDetailDB;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import model.Doctor;
import model.Sale;
import model.SaleDetail;
import model.SaleReturn;
import model.SaleReturnDetail;
import printing.POS;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class TabReturnController implements Initializable{

	@FXML
	private BorderPane borderPane;

	@FXML
	private JFXTextField txtReturnOrderId;

	@FXML
	private TableView<SaleReturnDetail> tblReturn;

	@FXML
	private TableColumn<SaleReturnDetail, Number> colReturnNo;

	@FXML
	private TableColumn<SaleReturnDetail, String> colReturnMedId;

	@FXML
	private TableColumn<SaleReturnDetail, String> colReturnMedicineName;

	@FXML
	private TableColumn<SaleReturnDetail, Number> colReturnQty;

	@FXML
	private TableColumn<SaleReturnDetail, Number> colReturnPrice;

	@FXML
	private TableColumn<SaleReturnDetail, Number> colReturnTotal;

	@FXML
	private TableColumn<SaleReturnDetail, Number> colSaleQty;
	
	
	@FXML
	private Label lbltotalAmount;

	@FXML
	private Label lblDiscountGiven;

	@FXML
	private Label lblReceiveAmount;
	
	@FXML
    private Label lbltotalReturnAmount;
	
	@FXML
	private Label lblDiscountAdjusted;

	@FXML
	private Label lblCashBack;


	private SaleDetailDB dbSaleDetail;
	private ObservableList<SaleDetail> saleDetailList = FXCollections.observableArrayList();
	private ObservableList<SaleReturnDetail> saleReturnDetailList;
	private Sale sale;
	private SaleReturn saleReturn;
	private int returnId = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sale = new Sale();
		dbSaleDetail = new SaleDetailDB();
		saleReturn = new SaleReturn();
		saleReturnDetailList = FXCollections.observableArrayList();

		Util.checkPositiveNumber(txtReturnOrderId);
		setTableCell();
		onSearchOrder();
	}

	@FXML
	public void btnReturnCancelAction(ActionEvent event) {
		cancel();
	}

	@FXML
	public void btnReturnUpdateAction(ActionEvent event) {
		//		save();
		boolean flag = false;
		for(SaleReturnDetail srd: saleReturnDetailList) {
			if(srd.getReturnQty()!= 0) {
				flag = true;
				break;
			}
		}
		if(flag) {
			save();
			POS.pageSetup(getPrintNode());
			cancel();
		}
		else {
			Dialog.error("Please Enter Return Quantity");
		}

	}
	
	private BorderPane getPrintNode()
	{
		BorderPane root = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/BillReturnPrint.fxml"));
			root = (BorderPane) fxmlLoader.load();
			BillReturnPrintController controller = (fxmlLoader).getController();
			
			if(sale.getSaleDoctor()==null) {
				sale.setSaleDoctor(new Doctor());
			}
			
			controller.setData(saleReturnDetailList, sale.getSaleCustomer().getCustName(),
					sale.getSaleDoctor().getDocName(),saleReturn.getReturnDate(),
					returnId, saleReturn.getReturnEmployee().getEmpName(), lbltotalAmount.getText(),
					lblDiscountGiven.getText(), lblReceiveAmount.getText(), 
					lbltotalReturnAmount.getText(), lblDiscountAdjusted.getText(), lblCashBack.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	@FXML
	public void btnReturnVoidAction(ActionEvent event) {

		if(!saleDetailList.isEmpty()) {
			dbSaleDetail.voidSale(sale, saleDetailList);
			Util.showNotification(borderPane, "Sale Void Successfully");
			cancel();
		}

	}

	private void setTableCell() {
		tblReturn.setEditable(true);
		colReturnNo.setCellFactory(new Callback<TableColumn<SaleReturnDetail,Number>, TableCell<SaleReturnDetail,Number>>() {

			@Override
			public TableCell<SaleReturnDetail, Number> call(TableColumn<SaleReturnDetail, Number> param) {
				return new SerialCell<>();
			}
		});
		colReturnMedId.setCellValueFactory(data -> data.getValue().returnStockDrugProperty().get().stockDrugProperty().get().drugUPCProperty());
		colReturnMedicineName.setCellValueFactory(data -> data.getValue().returnStockDrugProperty().get().stockDrugProperty().get().drugCommonNameProperty());
		colReturnQty.setCellValueFactory(data -> data.getValue().returnQtyProperty());
		colReturnQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		colReturnPrice.setCellValueFactory(data -> data.getValue().retailPriceProperty());
		colReturnTotal.setCellValueFactory(data -> data.getValue().totalPriceProperty());
		colSaleQty.setCellValueFactory(data -> data.getValue().saleQtyProperty());
		tblReturn.setItems(saleReturnDetailList);

		colReturnQty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SaleReturnDetail,Number>>() {

			@Override
			public void handle(CellEditEvent<SaleReturnDetail, Number> event) {
				SaleReturnDetail item = (SaleReturnDetail) event.getTableView().getItems().get(event.getTablePosition().getRow());
				item.setReturnQty(event.getNewValue().intValue());
				for(SaleReturnDetail srd : saleReturnDetailList) {
					if(srd.getReturnStockDrug().equals(item.getReturnStockDrug())) {
						srd.setReturnQty(item.getReturnQty());
					}
				}
				calculateTotal();
			}
		});


	}

	private void onSearchOrder() {
		txtReturnOrderId.setOnAction(event ->{
			sale = (Sale) DBUtil.getObject(Sale.class, "saleId", Integer.valueOf(txtReturnOrderId.getText()));
			saleReturnDetailList.clear();
			if(sale != null) {
				lbltotalAmount.setText(Util.decimalTwoDigitsString(sale.getTotalRetailPrice()));
				lblDiscountGiven.setText(Util.decimalTwoDigitsString(sale.getTotalDiscount()));
				lblReceiveAmount.setText(Util.decimalTwoDigitsString(sale.getTotalPayable()));
				saleReturn.setSaleReturnId(sale);
				saleDetailList = dbSaleDetail.getSaleDetail(sale.getSaleId());
				for(SaleDetail sd: saleDetailList) {
					SaleReturnDetail srd = new SaleReturnDetail();
					srd.setReturnStockDrug(sd.getSaleStockDrug());
					srd.setSaleQty(sd.getSaleQty());
					srd.setTradePrice(sd.getTradePrice());
					srd.setRetailPrice(sd.getRetailPrice());
					saleReturnDetailList.add(srd);
					calculateTotal();
				}
			}
		});
	}

	private void calculateTotal() {
		double discount = 100- ( ((sale.getTotalRetailPrice() - sale.getTotalDiscount()) * 100 ) / sale.getTotalRetailPrice() );
		
		DoubleBinding totalRetailPrice = Bindings.createDoubleBinding(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				return (double) Math.round(tblReturn.getItems().stream().collect(Collectors.summingDouble(new ToDoubleFunction<SaleReturnDetail>() {

					@Override
					public double applyAsDouble(SaleReturnDetail value) {
						double dis = discount/100;
						double d = value.getTotalPrice() * dis;
						return value.getTotalPrice() - d ;
					}
				})) );
			}
		}, tblReturn.getItems());
		lblCashBack.textProperty().bind(Bindings.format("%.2f", totalRetailPrice));
		saleReturn.setTotalRetailPrice(totalRetailPrice.doubleValue());
		
		DoubleBinding discountAdjusted = Bindings.createDoubleBinding(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				return tblReturn.getItems().stream().collect(Collectors.summingDouble(new ToDoubleFunction<SaleReturnDetail>() {

					@Override
					public double applyAsDouble(SaleReturnDetail value) {						
						return value.getTotalPrice();
					}
				}));
			}
		}, tblReturn.getItems());
		lbltotalReturnAmount.textProperty().bind(Bindings.format("%.2f", discountAdjusted));
		lblDiscountAdjusted.textProperty().bind(Bindings.format("%.2f", discountAdjusted.doubleValue() - totalRetailPrice.doubleValue()));


		DoubleBinding totalTradePrice  = Bindings.createDoubleBinding(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				return tblReturn.getItems().stream().collect(Collectors.summingDouble(new ToDoubleFunction<SaleReturnDetail>() {

					@Override
					public double applyAsDouble(SaleReturnDetail value) {
						return value.getTrade();
					}
				}));
			}
		}, tblReturn.getItems());
		saleReturn.setTotalTradePrice(totalTradePrice.doubleValue());
	}

	private void save() {
		if(!saleReturnDetailList.isEmpty()) {
			saleReturn.setReturnDate(new Date());
			saleReturn.setReturnTime(new Date());
			saleReturn.setReturnEmployee(LoginScreenController.emp);
			saleReturn.setTotalReturnProfit(saleReturn.getTotalRetailPrice() - saleReturn.getTotalTradePrice());
			SaleReturnDetailDB dbSaleReturnDetail = new SaleReturnDetailDB();
			returnId = dbSaleReturnDetail.createReturn(saleReturn, saleReturnDetailList);
			Util.showNotification(borderPane, "Sale Return Successfully");
//			cancel();
		}
		else {
			Dialog.error("Please Enter Valid Order Number");
		}
	}

	private void cancel() {
		saleDetailList.clear();
		saleReturnDetailList.clear();
		sale = null;
		sale = new Sale();
		saleReturn = null;
		saleReturn = new SaleReturn();
		txtReturnOrderId.clear();
		txtReturnOrderId.requestFocus();
		lbltotalAmount.setText("0.00");
		lblReceiveAmount.setText("0.00");
		lblDiscountGiven.setText("0.00");
		calculateTotal();
	}
}
