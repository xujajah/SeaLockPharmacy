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
import model.SaleDetail;
import utils.Util;

public class BillPrintController implements Initializable{
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
	private Label lblSubTotal;
	@FXML
	private Label lblDiscount;
	@FXML
	private Label lblGrandTotal;
	@FXML
	private Label lblPaymentCash;
	@FXML
	private Label lblChange;
	@FXML
	private Label lblTotalItems;
	@FXML
	private Label lblGreeting;
	@FXML
	private Label lblNotes;
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
		lblNotes.setText("Note: \n" + company.getBillNotes());
	}

	public void setData(ObservableList<SaleDetail> salesDetailList,String customerName, String doctorName, Date date,
			int billNo, String cashier,int totalItems, double subTotal, double discount, double grandTotal, double paymentCash, double change) {

		lblCustomer.setText("Customer : "+customerName);
		lblDoctor.setText("Ref By : "+ doctorName);
		lblDate.setText(String.valueOf(date));
		lblBillNo.setText("Bill No. : "+ billNo);
		lblCashier.setText("Cashier : "+cashier);
		lblTotalItems.setText("Items : " +totalItems);
		lblSubTotal.setText(Util.decimalTwoDigitsString(subTotal));
		lblDiscount.setText(Util.decimalTwoDigitsString(discount));
		lblGrandTotal.setText(Util.decimalTwoDigitsString(grandTotal));
		lblPaymentCash.setText(Util.decimalTwoDigitsString(paymentCash));
		lblChange.setText(Util.decimalTwoDigitsString(change));
		for(SaleDetail sd : salesDetailList) {
			Label product = new Label(sd.getSaleStockDrug().getStockDrug().getDrugCommonName());
			product.setFont(new Font(8));
			vboxProductName.getChildren().add(product);

			Label qty = new Label(sd.getSaleQty()+"");
			qty.setFont(new Font(8));
			vboxQty.getChildren().add(qty);

			Label price = new Label(Util.decimalTwoDigitsString(sd.getRetailPrice())+"");
			price.setFont(new Font(8));
			vboxRetail.getChildren().add(price);

			Label total = new Label(Util.decimalTwoDigitsString(sd.getTotalRetailPrice())+"");
			total.setFont(new Font(8));
			vboxTotal.getChildren().add(total);
		}
	}

}
