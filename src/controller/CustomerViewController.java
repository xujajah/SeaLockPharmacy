package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import database.DataBase;
import interfaces.Notification;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
import model.Customer;
import model.Employee;
import model.Province;
import model.Region;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;



public class CustomerViewController implements Initializable {
	@FXML
	private BorderPane rootCustomerView;

	@FXML
	private HBox barColor;

	@FXML
	private TableView<Customer> tblViewCustomer;

	@FXML
	private TableColumn<Customer, Number> colSerial;

	@FXML
	private TableColumn<Customer, Number> colID;

	@FXML
	private TableColumn<Customer, String> colCustomerName;

	@FXML
	private TableColumn<Customer, String> colIdentity;

	@FXML
	private TableColumn<Customer, String> colAddress;

	@FXML
	private TableColumn<Customer, String> colPhone;

	@FXML
	private TableColumn<Customer, Province> colProvince;

	@FXML
	private TableColumn<Customer, Region> colRegion;

	@FXML
	private TableColumn<Customer, Date> colDOB;

	@FXML
	private TableColumn<Customer, String> colEmail;

	@FXML
	private TableColumn<Customer, Employee> colEmployee;

	@FXML
	private TableColumn<Customer, Customer> colAction;

	@FXML
	private TextField txtSearch;

	@FXML
	private JFXButton btnAdd;

	DataBase db = new DataBase();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setTableCell();
		setAction();
		filterSearch();
		mouseClick();
	}

	@FXML
	public void btnAddAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CustomerRegistration.fxml"));
			Parent root = fxmlLoader.load();
			CustomerRegistrationController crc = (fxmlLoader).getController();
			crc.sendReferance(db, new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(rootCustomerView, msg);
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

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<Customer,Number>, TableCell<Customer,Number>>() {

			@Override
			public TableCell<Customer, Number> call(TableColumn<Customer, Number> param) {
				return new SerialCell<>();
			}
		});
		colID.setCellValueFactory(data -> data.getValue().custIdProperty());
		colCustomerName.setCellValueFactory(data -> data.getValue().custNameProperty());
		colIdentity.setCellValueFactory(data -> data.getValue().custIdentityProperty());
		colAddress.setCellValueFactory(data -> data.getValue().custAddressProperty());
		colDOB.setCellValueFactory(data -> data.getValue().custDOBProperty());
		colPhone.setCellValueFactory(data -> data.getValue().custPhoneProperty());
		colEmail.setCellValueFactory(data -> data.getValue().custEmailProperty());
		colProvince.setCellValueFactory(data -> data.getValue().custRegionProperty().get().regionProvinceProperty());
		colRegion.setCellValueFactory(data -> data.getValue().custRegionProperty());
		colEmployee.setCellValueFactory(data -> data.getValue().custEmployeeProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private void setAction() {

		colAction.setCellFactory(param -> new TableCell<Customer,Customer>() {
			@Override
			protected void updateItem(Customer customer, boolean empty) {
				super.updateItem(customer, empty);

				if (customer == null) {
					setGraphic(null);
					return;
				}
				HBox hb;
				Button deleteButton;
				Button updateButton;
				hb = new HBox();

				hb.setPadding(new Insets(0, 3, 0, 3));
				//15, 12, 15, 12
				hb.setSpacing(10);
				deleteButton = new Button();
				updateButton = new Button();

				Image image = new Image("/media/delete.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);

				Image update = new Image("/media/edit.png", 20, 20, true, true);
				ImageView updateImageView = new ImageView(update);
				updateImageView .setFitWidth(20);
				updateImageView .setFitHeight(20);

				updateButton.setGraphic(updateImageView );
				updateButton.setStyle("-fx-background-color: transparent;");
				deleteButton.setGraphic(imageView);
				deleteButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(updateButton);
				hb.getChildren().add(deleteButton);
				setGraphic(hb);
				updateButton.setOnAction(event -> {      	
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CustomerUpdate.fxml"));
						Parent root = fxmlLoader.load();
						CustomerUpdateController cuc = (fxmlLoader).getController();
						cuc.setCustomer(customer);
						cuc.sendReferance(db,new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootCustomerView, msg);
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

				deleteButton.setOnAction(
						event -> {
							if(Dialog.confirmAlert(null, "Are you Sure! you want to delete")){
								customer.setCustActive(false);
								customer.setCustEmployee(LoginScreenController.emp);
								db.softDelete(customer);
								Util.showNotification(rootCustomerView, "Customer Deleted Successfully");
							}
						}
						);
			}
		});
	}

	private void mouseClick() {
		tblViewCustomer.setRowFactory(tr -> {
			TableRow<Customer> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblViewCustomer.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblViewCustomer.getItems().size() && tblViewCustomer.getSelectionModel().isSelected(index)  ) {
								tblViewCustomer.getSelectionModel().clearSelection();
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
		FilteredList<Customer> filteredList = new FilteredList<>(db.retrieve(Customer.class,"custActive",true), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(customer -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return customer.getCustName().toLowerCase().contains(lowerCaseFilter)
						|| customer.getCustAddress().toLowerCase().contains(lowerCaseFilter)
						|| customer.getCustPhone().toLowerCase().contains(lowerCaseFilter)
						|| customer.getCustRegion().getRegionProvince().getProvinceName().toLowerCase().contains(lowerCaseFilter)
						|| customer.getCustRegion().getRegionName().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Customer> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblViewCustomer.comparatorProperty());
		tblViewCustomer.setItems(sortedList);
	}

}
