package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Employee;
import model.Supplier;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.DataBase;
import interfaces.Notification;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class SupplierViewController implements Initializable{
	@FXML
	private BorderPane rootSupplierView;

	@FXML
	private TableView<Supplier> tblSupplier;

	@FXML
	private TableColumn<Supplier, Number> colSerial;

	@FXML
	private TableColumn<Supplier, Number> colID;

	@FXML
	private TableColumn<Supplier, String> colName;

	@FXML
	private TableColumn<Supplier, String> colPhone;

	@FXML
	private TableColumn<Supplier,Employee> colEmployee;

	@FXML
	private TableColumn<Supplier, String> colAddress;

	@FXML
	private TableColumn<Supplier, String> colEmail;

	@FXML
	private TableColumn<Supplier, String> colWebsite;

	@FXML
	private TableColumn<Supplier, Supplier> colAction;

	@FXML
	private TextField txtSearch;

	private DataBase db = new DataBase();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTableCell();
		setAction();
		filterSearch();
		mouseClick();

	}

	private void setTableCell() {
		colSerial.setCellFactory(new Callback<TableColumn<Supplier,Number>, TableCell<Supplier,Number>>() {
			@Override
			public TableCell<Supplier, Number> call(TableColumn<Supplier, Number> param) {
				return new SerialCell<>();
			}
		});
		colID.setCellValueFactory(data -> data.getValue().suppIdProperty());
		colName.setCellValueFactory(data -> data.getValue().suppNameProperty());
		colAddress.setCellValueFactory(data -> data.getValue().suppAddressProperty());
		colPhone.setCellValueFactory(data -> data.getValue().suppPhoneProperty());
		colEmployee.setCellValueFactory(data -> data.getValue().suppEmployeeProperty());
		colEmail.setCellValueFactory(data -> data.getValue().suppEmailProperty());
		colWebsite.setCellValueFactory(data -> data.getValue().suppWebsiteProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	@FXML
	public void btnAddSupplierAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SupplierRegistration.fxml"));
			Parent root = fxmlLoader.load();
			SupplierRegistrationController sp = (fxmlLoader).getController();
			sp.sendReferance(db,new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(rootSupplierView, msg);
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
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SupplierUpdate.fxml"));
						Parent root = fxmlLoader.load();
						SupplierUpdateController suc = (fxmlLoader).getController();
						suc.setSupplier(supplier);
						suc.sendReferance(db,new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootSupplierView, msg);
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
								supplier.setSuppEmployee(LoginScreenController.emp);
								supplier.setSuppActive(false);
								db.softDelete(supplier);
								Util.showNotification(rootSupplierView, "Supplier Deleted Successfully");
							}

						}
						);

			}
		});
	}

	private void mouseClick() {
		tblSupplier.setRowFactory(tr -> {
			TableRow<Supplier> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblSupplier.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblSupplier.getItems().size() && tblSupplier.getSelectionModel().isSelected(index)  ) {
								tblSupplier.getSelectionModel().clearSelection();
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
		FilteredList<Supplier> filteredList = new FilteredList<>(db.retrieve(Supplier.class,"suppActive",true), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(supplier -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return supplier.getSuppName().toLowerCase().contains(lowerCaseFilter)
						|| supplier.getSuppAddress().toLowerCase().contains(lowerCaseFilter)
						|| supplier.getSuppPhone().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Supplier> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblSupplier.comparatorProperty());
		tblSupplier.setItems(sortedList);
	}

}
