package controller;

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
import model.Doctor;
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

public class DoctorViewController implements Initializable {
	@FXML
	private BorderPane rootDoctorView;

	@FXML
	private TableView<Doctor> tblViewDoctor;

	@FXML
	private TableColumn<Doctor, Number> colSerial;

	@FXML
	private TableColumn<Doctor, Number> colID;

	@FXML
	private TableColumn<Doctor, String> colName;

	@FXML
	private TableColumn<Doctor, String> colIdentity;
	
	@FXML
	private TableColumn<Doctor, Employee> colEmployee;

	@FXML
	private TableColumn<Doctor, String> colAddress;

	@FXML
	private TableColumn<Doctor, String> colPhone;

	@FXML
	private TableColumn<Doctor, String> colEmail;

	@FXML
	private TableColumn<Doctor, String> colAffiliation;

	@FXML
	private TableColumn<Doctor, Doctor> colAction;

	@FXML
	private TextField txtSearch;

	private DataBase db = new DataBase();

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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DoctorRegistration.fxml"));
			Parent root = fxmlLoader.load();
			DoctorRegistrationController drc = (fxmlLoader).getController();
			drc.sendReferance(db,new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(rootDoctorView, msg);
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
		colSerial.setCellFactory(new Callback<TableColumn<Doctor,Number>, TableCell<Doctor,Number>>() {
			@Override
			public TableCell<Doctor, Number> call(TableColumn<Doctor, Number> param) {
				return new SerialCell<>();
			}
		});
		colID.setCellValueFactory(data -> data.getValue().docIdProperty());
		colName.setCellValueFactory(data -> data.getValue().docNameProperty());
		colIdentity.setCellValueFactory(data -> data.getValue().docIdentityProperty());
		colEmployee.setCellValueFactory(data -> data.getValue().docEmployeeProperty());
		colAddress.setCellValueFactory(data -> data.getValue().docAddressProperty());
		colPhone.setCellValueFactory(data -> data.getValue().docPhoneProperty());
		colEmail.setCellValueFactory(data -> data.getValue().docEmailProperty());
		colAffiliation.setCellValueFactory(data -> data.getValue().docAffiliationProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private void setAction() {

		colAction.setCellFactory(param -> new TableCell<Doctor, Doctor>() {
			@Override
			protected void updateItem(Doctor doctor, boolean empty) {
				super.updateItem(doctor, empty);

				if (doctor == null) {
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
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DoctorUpdate.fxml"));
						Parent root = fxmlLoader.load();
						DoctorUpdateController duc = (fxmlLoader).getController();
						duc.setDoctor(doctor);
						duc.sendReferance(db,new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootDoctorView, msg);
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
								doctor.setDocActive(false);
								doctor.setDocEmployee(LoginScreenController.emp);
								db.softDelete(doctor);
								Util.showNotification(rootDoctorView, "Doctor Deleted Successfully");
							}

						}
						);
			}
		});
	}

	private void mouseClick() {
		tblViewDoctor.setRowFactory(tr -> {
			TableRow<Doctor> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblViewDoctor.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblViewDoctor.getItems().size() && tblViewDoctor.getSelectionModel().isSelected(index)  ) {
								tblViewDoctor.getSelectionModel().clearSelection();
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
		FilteredList<Doctor> filteredList = new FilteredList<>(db.retrieve(Doctor.class,"docActive",true), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(doctor -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return doctor.getDocName().toLowerCase().contains(lowerCaseFilter)
						|| doctor.getDocAddress().toLowerCase().contains(lowerCaseFilter)
						|| doctor.getDocPhone().toLowerCase().contains(lowerCaseFilter)
						|| doctor.getDocAffiliation().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Doctor> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblViewDoctor.comparatorProperty());
		tblViewDoctor.setItems(sortedList);
	}



}
