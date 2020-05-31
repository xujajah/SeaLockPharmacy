package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import database.DataBase;
import interfaces.DoctorInterface;
import interfaces.Notification;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Doctor;
import utils.Util;

public class DoctorsController implements Initializable{


    @FXML
    private BorderPane mainBorder;
    
    @FXML
    private HBox barColor;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TableView<Doctor> tblDoctos;

    @FXML
    private TableColumn<Doctor, String> colName;

    @FXML
    private TableColumn<Doctor, String> colIdentity;

    @FXML
    private TableColumn<Doctor, String> colPhone;

    @FXML
    private TableColumn<Doctor, String> colAddress;

    @FXML
    private TableColumn<Doctor, String> colAffiliation;

    @FXML
    private TableColumn<Doctor, Doctor> colAction;
    
    private DoctorInterface doctorCallback;
    private DataBase dataBase;
    private KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
	private KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE);
	private KeyCombination tab = new KeyCodeCombination(KeyCode.TAB);
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	setTableCell();
		mouseClick();
		setAction();
		enterCustomer();
		shortCutKeys();
	}


    @FXML
    public void btnAddAction(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DoctorRegistration.fxml"));
			Parent root = fxmlLoader.load();
			DoctorRegistrationController drc = (fxmlLoader).getController();
			drc.sendReferance(dataBase,new Notification() {

				@Override
				public void showNotification(String msg) {
					Util.showNotification(barColor, msg);
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
		colName.setCellValueFactory(data -> data.getValue().docNameProperty());
		colIdentity.setCellValueFactory(data -> data.getValue().docIdentityProperty());
		colPhone.setCellValueFactory(data -> data.getValue().docPhoneProperty());
		colAddress.setCellValueFactory(data -> data.getValue().docAddressProperty());	
		colAffiliation.setCellValueFactory(data -> data.getValue().docAffiliationProperty());
		colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	}

    private <T> void filterSearch() {
		@SuppressWarnings("unchecked")
		ObservableList<Doctor> list = (ObservableList<Doctor>) ((ObservableList<T>)dataBase.getList());
		FilteredList<Doctor> filteredList = new FilteredList<>(list, p -> true);
		txtSearch.textProperty().addListener((observable,oldValue,newValue) -> {
			filteredList.setPredicate(doc -> {
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				return doc.getDocName().toLowerCase().contains(lowerCaseFilter)
						|| doc.getDocAddress().toLowerCase().contains(lowerCaseFilter)
						|| doc.getDocAffiliation().toLowerCase().contains(lowerCaseFilter)
						|| doc.getDocPhone().toLowerCase().contains(lowerCaseFilter);
			});
		});
		SortedList<Doctor> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tblDoctos.comparatorProperty());
		tblDoctos.setItems(sortedList);
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
				Button selectButton;
				hb = new HBox();

				hb.setPadding(new Insets(0, 3, 0, 3));
				//15, 12, 15, 12
				hb.setSpacing(10);
				selectButton = new Button();

				Image image = new Image("/media/select.png", 20, 20, true, true);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(20);
				imageView.setFitHeight(20);

				selectButton.setGraphic(imageView);
				selectButton.setStyle("-fx-background-color: transparent;");

				hb.getChildren().add(selectButton);
				setGraphic(hb);
				selectButton.setOnAction(event -> {
					doctorCallback.setDoctor(doctor);
					Util.hideWindow(event);
				});
			}
		});
	}
    
    private void mouseClick() {
		tblDoctos.setRowFactory(tr -> {
			TableRow<Doctor> row = new TableRow<>();
			row.setOnMouseClicked(e ->{
				if(row.isEmpty())
				{
					tblDoctos.getSelectionModel().clearSelection();
				}
				else {
					row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
						@Override  
						public void handle(MouseEvent event) {  
							final int index = row.getIndex();  
							if (index >= 0 && index < tblDoctos.getItems().size() && tblDoctos.getSelectionModel().isSelected(index)  ) {
								tblDoctos.getSelectionModel().clearSelection();
								event.consume();  
							}  
						}  
					});
				}
			});

			return row;
		});
	}

	private void enterCustomer(){
		tblDoctos.setOnKeyPressed(event ->{
			if(enter.match(event)){
				int index = tblDoctos.getSelectionModel().getSelectedIndex();
			doctorCallback.setDoctor(tblDoctos.getItems().get(index));
				Util.hideWindow(event);
			}
		});
	}

	public void sendReference(DataBase dataBase,boolean btn, DoctorInterface callBack) {
		this.dataBase = dataBase;
		this.doctorCallback= callBack;
		btnAdd.setVisible(btn);
		filterSearch();
	}
	
	private void shortCutKeys() {
		txtSearch.getParent().setOnKeyPressed(event ->{
			if(escape.match(event)) {
				Util.hideWindow(event);
			}
		});
		
		txtSearch.setOnKeyPressed(event ->{
			if(tab.match(event)) {
				tblDoctos.requestFocus();
				tblDoctos.getSelectionModel().select(0);
				tblDoctos.getFocusModel().focus(0);
				btnAdd.setFocusTraversable(false);
			}
		});
	}
	@FXML
    void btnCrossAction(ActionEvent event) {
    	mainBorder.getScene().getWindow().hide();
    }
}
