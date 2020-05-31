package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import database.DataBase;
import interfaces.Notification;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import model.Employee;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class EmployeeViewController implements Initializable {
	@FXML
	private BorderPane rootEmployeeView;

	@FXML
	private HBox barColor;

	@FXML
	private TableView<Employee> tblViewEmployee;

	@FXML
	private TableColumn<Employee, Number> colID;

	@FXML
	private TableColumn<Employee, String> colName;

	@FXML
	private TableColumn<Employee, String> colIdentity;

	@FXML
	private TableColumn<Employee, String> colAddress;

	@FXML
	private TableColumn<Employee, String> colPhone;

	@FXML
	private TableColumn<Employee, String> colEmail;

	@FXML
	private TableColumn<Employee, String> colCnic;

	@FXML
	private TableColumn<Employee, Number> colSerial;

	@FXML
	private TableColumn<Employee, Date> colDOB;

	@FXML
	private TableColumn<Employee, String> colUserName;

	@FXML
	private TableColumn<Employee, String> colPassword;

	@FXML
	private TableColumn<Employee, String> colDesignation;

	@FXML
	private TableColumn<Employee, Employee> colAction;

	@FXML
	private TextField txtSearch;

	@FXML
	private JFXButton btnAdd;

	DataBase db = new DataBase();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTableCell();
		setAction();
		filterSearch();
		mouseClick();
	}

	@FXML
	public void btnAddAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EmployeeRegistration.fxml"));
			Parent root = fxmlLoader.load();
			EmployeeRegistrationController erc = (fxmlLoader).getController();
			erc.sendReferance(db, new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(rootEmployeeView, msg);
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
		colSerial.setCellFactory(new Callback<TableColumn<Employee,Number>, TableCell<Employee,Number>>() {

			@Override
			public TableCell<Employee, Number> call(TableColumn<Employee, Number> param) {
				return new SerialCell<>();
			}
		});
		colID.setCellValueFactory(data -> data.getValue().empIdProperty());
		colName.setCellValueFactory(data -> data.getValue().empNameProperty());
		colIdentity.setCellValueFactory(data -> data.getValue().empIdentityProperty());
		colAddress.setCellValueFactory(data -> data.getValue().empAddressProperty());
		colDOB.setCellValueFactory(data -> data.getValue().empDOBProperty());
		colPhone.setCellValueFactory(data -> data.getValue().empPhoneProperty());
		colEmail.setCellValueFactory(data -> data.getValue().empEmailProperty());
		colCnic.setCellValueFactory(data -> data.getValue().empCNICProperty());
		colDesignation.setCellValueFactory(data -> data.getValue().empDesignationProperty());
		colUserName.setCellValueFactory(data -> data.getValue().empUserNameProperty());
		colPassword.setCellValueFactory(data -> data.getValue().empPswdProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private void setAction() {

		colAction.setCellFactory(param -> new TableCell<Employee, Employee>() {
			@Override
			protected void updateItem(Employee employee, boolean empty) {
				super.updateItem(employee, empty);

				if (employee == null) {
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
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EmployeeUpdate.fxml"));
						Parent root = fxmlLoader.load();
						EmployeeUpdateController euc = (fxmlLoader).getController();
						euc.setEmployee(employee);
						euc.sendReferance(db,new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootEmployeeView, msg);
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
								employee.setEmpActive(false);
								db.softDelete(employee);
								Util.showNotification(rootEmployeeView, "Employee Deleted Successfully");
							}
						}
						);
			}
		});
	}

	private void mouseClick() {
		tblViewEmployee.setRowFactory(tr -> {
			TableRow<Employee> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblViewEmployee.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblViewEmployee.getItems().size() && tblViewEmployee.getSelectionModel().isSelected(index)  ) {
								tblViewEmployee.getSelectionModel().clearSelection();
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
		FilteredList<Employee> filteredList = new FilteredList<>(db.retrieve(Employee.class,"empActive",true), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(employee -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return employee.getEmpName().toLowerCase().contains(lowerCaseFilter)
						|| employee.getEmpAddress().toLowerCase().contains(lowerCaseFilter)
						|| employee.getEmpPhone().toLowerCase().contains(lowerCaseFilter)
						|| employee.getEmpCNIC().toLowerCase().contains(lowerCaseFilter)
						|| employee.getEmpUserName().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Employee> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblViewEmployee.comparatorProperty());
		tblViewEmployee.setItems(sortedList);
	}

}
