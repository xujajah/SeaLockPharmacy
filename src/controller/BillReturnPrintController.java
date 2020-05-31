package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import database.DBUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Company;
import model.SaleReturnDetail;
import utils.Util;

public class BillReturnPrintController implements Initializable{
	@FXML
	private VBox topVbox;
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblAddress1;
	@FXML
	private Label lblAddress2;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblCustomer;
	@FXML
	private Label lblDoctor;
	@FXML
	private Label lblDate;
	@FXML
	private Label lblBillNo;
	@FXML
	private Label lblCashier;
	@FXML
	private Label lblTotalAmount;
	@FXML
	private Label lblDiscountGiven;
	@FXML
	private Label lblReceivedAmount;
	@FXML
	private Label lblTotalReturnAmount;
	@FXML
	private Label lblDiscountAdjusted;
	@FXML
	private Label lblCashBack;
	@FXML
	private Label lblGreeting;
	@FXML
	private Label lblDeveloper;
	@FXML
	private VBox vboxProductName;
	@FXML
	private VBox vboxRetail;
	@FXML
	private VBox vboxQty;
	@FXML
	private VBox vboxTotal;

	Company company;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		company = new Company();
		company = (Company) DBUtil.getObject(Company.class, 1);
		lblTitle.setText(company.getName());
		lblAddress1.setText(company.getAddress());
		lblAddress2.setText(company.getCity());
		lblPhone.setText(company.getPhone());
	}

	//	public void setData(ObservableList<SaleReturnDetail> salesReturnDetailList,String customerName, String doctorName, Date date,
	//			int billNo, String cashier, double totalAmount, double discountGiven, double receivedAmount, double totalReturnAmount,
	//			double discountAdjustment, double cashBack) {

	public void setData(ObservableList<SaleReturnDetail> salesReturnDetailList,String customerName, String doctorName, Date date,
			int billNo, String cashier, String totalAmount, String discountGiven, String receivedAmount, String totalReturnAmount,
			String discountAdjustment, String cashBack) {
		lblCustomer.setText("Customer : "+customerName);
		lblDoctor.setText("Ref By : "+ doctorName);
		lblDate.setText(String.valueOf(date));
		lblBillNo.setText("Bill No. : "+ billNo);
		lblCashier.setText("Cashier : "+cashier);
		lblTotalAmount.setText(totalAmount);
		lblDiscountGiven.setText(discountGiven);
		lblReceivedAmount.setText(receivedAmount);
		lblTotalReturnAmount.setText(totalReturnAmount);
		lblDiscountAdjusted.setText(discountAdjustment);
		lblCashBack.setText(cashBack);
		for(SaleReturnDetail srd : salesReturnDetailList) {
			Label product = new Label(srd.getReturnStockDrug().getStockDrug().getDrugCommonName());
			product.setFont(new Font(8));
			vboxProductName.getChildren().add(product);

			Label qty = new Label(srd.getReturnQty()+"");
			qty.setFont(new Font(8));
			vboxQty.getChildren().add(qty);

			Label price = new Label(Util.decimalTwoDigitsString(srd.getRetailPrice())+"");
			price.setFont(new Font(8));
			vboxRetail.getChildren().add(price);

			Label total = new Label(Util.decimalTwoDigitsString(srd.getTotalPrice())+"");
			total.setFont(new Font(8));
			vboxTotal.getChildren().add(total);
		}
	}


}
