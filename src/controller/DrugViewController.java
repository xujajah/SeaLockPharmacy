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
import model.Drug;
import model.Employee;
import utils.Dialog;
import utils.SerialCell;
import utils.Util;

public class DrugViewController implements Initializable{
	@FXML
	private BorderPane rootDrugView;

	@FXML
	private TableView<Drug> tblViewDrug;

	@FXML
	private TableColumn<Drug, Number> colSerial;

	@FXML
	private TableColumn<Drug, String> colUpc;

	@FXML
	private TableColumn<Drug, String> colComName;

	@FXML
	private TableColumn<Drug, String> colMedName;

	@FXML
	private TableColumn<Drug, Employee> colEmployee;

	@FXML
	private TableColumn<Drug, String> colCategory;

	@FXML
	private TableColumn<Drug, String> colDosage;

	@FXML
	private TableColumn<Drug, String> colDescription;

	@FXML
	private TableColumn<Drug, String> colControlDrug;

	@FXML
	private TableColumn<Drug, Number> colQtyPerPack;

	@FXML
	private TableColumn<Drug, String> colManufacturer;

	@FXML
	private TableColumn<Drug, Drug> colAction;

	@FXML
	private TextField txtSearch;

	DataBase db = new DataBase();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setTableCell();
		setAction();
		mouseClick();
		filterSearch();
	}

	@FXML
	public void btnAddAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DrugRegistration.fxml"));
			Parent root = fxmlLoader.load();
			DrugRegistrationController drc = (fxmlLoader).getController();
			drc.sendReferance(db,new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(rootDrugView, msg);

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
		colSerial.setCellFactory(new Callback<TableColumn<Drug,Number>, TableCell<Drug,Number>>() {
			@Override
			public TableCell<Drug, Number> call(TableColumn<Drug, Number> param) {
				return new SerialCell<>();
			}
		});
		colUpc.setCellValueFactory(data -> data.getValue().drugUPCProperty());
		colComName.setCellValueFactory(data -> data.getValue().drugCommonNameProperty());
		colMedName.setCellValueFactory(data -> data.getValue().drugMedicalNameProperty());
		colEmployee.setCellValueFactory(data -> data.getValue().drugEmployeeProperty());
		colCategory.setCellValueFactory(data -> data.getValue().drugCategoryProperty());
		colDosage.setCellValueFactory(data -> data.getValue().drugDosageProperty());
		colDescription.setCellValueFactory(data -> data.getValue().drugDescriptionProperty());
		colControlDrug.setCellValueFactory(data -> data.getValue().drugControlDrugProperty());
		colQtyPerPack.setCellValueFactory(data -> data.getValue().drugQtyPerPackProperty());
		colManufacturer.setCellValueFactory(data -> data.getValue().drugManufacturerProperty());	    	
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

	private void setAction() {
		colAction.setCellFactory(param -> new TableCell<Drug, Drug>() {
			@Override
			protected void updateItem(Drug drug, boolean empty) {
				super.updateItem(drug, empty);

				if (drug == null) {
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
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DrugUpdate.fxml"));
						Parent root = fxmlLoader.load();
						DrugUpdateController duc = (fxmlLoader).getController();
						duc.setDrug(drug);
						duc.sendReferance(db,new Notification() {

							@Override
							public void showNotification(String msg) {
								Util.showNotification(rootDrugView, msg);

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
								drug.setDrugActive(false);
								drug.setDrugEmployee(LoginScreenController.emp);
								db.softDelete(drug);
								Util.showNotification(rootDrugView, "Drug Deleted Successfully");
							}

						}
						);

			}
		});
	}

	private void mouseClick() {
		tblViewDrug.setRowFactory(tr -> {
			TableRow<Drug> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblViewDrug.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblViewDrug.getItems().size() && tblViewDrug.getSelectionModel().isSelected(index)  ) {
								tblViewDrug.getSelectionModel().clearSelection();
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
		FilteredList<Drug> filteredList = new FilteredList<>(db.retrieve(Drug.class,"drugActive",true), p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(drug -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return drug.getDrugCommonName().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugMedicalName().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugManufacturer().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugControlDrug().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugCategory().toLowerCase().contains(lowerCaseFilter)
						|| drug.getDrugUPC().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Drug> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblViewDrug.comparatorProperty());
		tblViewDrug.setItems(sortedList);
	}

}
