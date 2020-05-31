package controller;

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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoginHistory;
import utils.Dialog;
import utils.Util;

public class MainWindowController implements Initializable{
	@FXML
	private BorderPane mainBorder;
	@FXML
	private JFXButton btnDashboard;
	@FXML
	private JFXButton btnSales;
	@FXML
	private JFXButton btnCustomer;
	@FXML
	private JFXButton btnDoctor;
	@FXML
	private JFXButton btnEmployee;
	@FXML
	private JFXButton btnSupplier;
	@FXML
	private JFXButton btnPurchase;
	@FXML
	private JFXButton btnInventory;
	@FXML
	private JFXButton btnReport;
	@FXML
	private JFXButton btnPrescription;
	@FXML
	private JFXButton btnOthers;
	@FXML
	private Tab tabitemFile;
	@FXML
	private JFXButton logout;
	@FXML
	private Tab tabitemEdit;
	@FXML
	private JFXButton theme1;
	@FXML
	private JFXButton theme2;
	@FXML
	private JFXButton theme3;
	@FXML
	private JFXButton theme4;
	@FXML
	private JFXButton theme5;
	@FXML
	private JFXButton theme6;
	@FXML
	private JFXButton theme7;
	@FXML
	private Tab tabSettings;
	@FXML
	private JFXButton btnConfiguration;
	@FXML
	private JFXButton btnPrinter;
	@FXML
	private JFXButton btnMinus;
	@FXML
	private JFXButton btnSquare;
	@FXML
	private JFXButton btnCross;

	private HBox selectedBox;
	private String stt = "";
	private String title ="";
	private boolean isMax;
	private double width = 650;
	private double height = 1100;
	private Label label;
	private DataBase dbTheme;
	private Stage stage;
	double xOffset ;
	double yOffset ;
	Boolean resizebottom = false;
	double dx;
	double dy;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbTheme = new DataBase();
		isMax = false;
		showDashboard();
		buttonFocus(btnDashboard);
	}

	// Event Listener on JFXButton[#btnDashboard].onAction
	@FXML
	public void handleSelection(ActionEvent event) {
		Tooltip tp = new Tooltip("Please Wait..!");
		javafx.geometry.Bounds bounds = mainBorder.localToScreen(mainBorder.getBoundsInLocal());
		tp.setMaxWidth(Double.MAX_VALUE);
		tp.setMaxHeight(70);
		tp.setFont(Font.font(18));
		tp.show(mainBorder,bounds.getMinX()+170,bounds.getMinY()+100);


		JFXButton button = (JFXButton) event.getSource();

		switch (button.getId()) {
		case "btnDashboard":
			stt="/views/Dashboard.fxml";
			title = "Dashboard";
			break;	
		case "btnCustomer":
			stt="/views/CustomerView.fxml";
			title = "Customer";
			break;	

		case "btnDoctor":
			stt="/views/DoctorView.fxml";
			title = "Doctor";
			break;

		case "btnInventory":
			stt="/views/Inventory.fxml";
			title = "Inventory";
			break;

		case "btnSales":
			stt="/views/Sale.fxml";
			title = "POS";
			break;

		case "btnEmployee":
			stt="/views/EmployeeView.fxml";
			title = "Employee";
			break;

		case "btnSupplier":
			stt="/views/SupplierView.fxml";
			title = "Supplier";
			break;

		case "btnPurchase":
			stt="/views/Purchase.fxml";
			title = "Purchase";
//			Stage stage = ((Stage) ((Button)event.getSource()).getScene().getWindow());
//			stage.setMaximized(true);
			break;

		case "btnReport":
			stt="/views/TabViewReport.fxml";
			title = "Report";
			break;


		case "btnPrescription":
			stt="/views/PrescriptionView.fxml";
			title = "Prescription";
			break;

		case "btnOthers":
			stt="/views/Theme.fxml";
			title = "Others";
			break;

		default:
			stt="/views/Dashboard.fxml";
			title = "Dashboard";
			break;
		}//Switch


		Service<String> service= new Service<String>() {

			Parent root = null;
			@Override
			protected Task<String> createTask() {


				Task<String> task =  new Task<String>() {

					@Override
					protected String call() throws Exception {
						FXMLLoader loader = new FXMLLoader(getClass().getResource(stt));

						try {

							root = loader.load();

						} catch (IOException e) {
							e.printStackTrace();
						}

						//	tp.show(null);
						return null;
					}
				};


				task.setOnSucceeded(evt -> {
					mainBorder.setCenter(root);
					stage.setTitle(title);

					tp.hide();

				});
				return task;
			}};

			service.start();	


			if(selectedBox != null)
			{
				selectedBox.getChildren().remove(1);
			}
			HBox box = (HBox) ((Button)event.getSource()).getParent();
			label = new Label();
			label.setPrefHeight(btnDashboard.getHeight());
			label.setPrefWidth(8);
			label.setBackground(new Background( new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
			box.getChildren().add(label);
			selectedBox = box;    
	}
	// Event Listener on Tab[#tabitemFile].onSelectionChanged
	@FXML
	public void viewTabitemFile(Event event) {
	}
	// Event Listener on JFXButton[#logout].onAction
	@FXML
	public void logout(ActionEvent event) {
		LoginHistory loginHistory = new LoginHistory();
		loginHistory.setDate(new Date());
		loginHistory.setTime(new Date());
		loginHistory.setEmployee(LoginScreenController.emp);
		loginHistory.setStatus("Logout Successfully");
		new DataBase().create(loginHistory);

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/LoginScreen.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = new Stage() ;
			scene.getStylesheets().add(getClass().getResource("/application/application1.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/media/logo.png"));
			primaryStage.setTitle("LoginScreen");
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
			mainBorder.getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Tab[#tabitemEdit].onSelectionChanged
	@FXML
	public void viewTabitemEdit(Event event) {
	}
	// Event Listener on JFXButton[#theme1].onAction
	@FXML
	public void ThemeSelect1(ActionEvent event) {
		LoginScreenController.theme.setThemeId(1);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme2].onAction
	@FXML
	public void ThemeSelect2(ActionEvent event) {
		LoginScreenController.theme.setThemeId(2);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme3].onAction
	@FXML
	public void ThemeSelect3(ActionEvent event) {
		LoginScreenController.theme.setThemeId(3);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme4].onAction
	@FXML
	public void ThemeSelect4(ActionEvent event) {
		LoginScreenController.theme.setThemeId(4);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme5].onAction
	@FXML
	public void ThemeSelect5(ActionEvent event) {
		LoginScreenController.theme.setThemeId(5);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme6].onAction
	@FXML
	public void ThemeSelect6(ActionEvent event) {
		LoginScreenController.theme.setThemeId(6);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on JFXButton[#theme7].onAction
	@FXML
	public void ThemeSelect7(ActionEvent event) {
		LoginScreenController.theme.setThemeId(7);
		dbTheme.update(LoginScreenController.theme);
		changeTheme(event);
	}
	// Event Listener on Tab[#tabSettings].onSelectionChanged
	@FXML
	public void viewTabitemSettings(Event event) {
	}
	// Event Listener on JFXButton[#btnConfiguration].onAction
	@FXML
	public void configurationAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CompanyRegistration.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Event Listener on JFXButton[#btnPrinter].onAction
	@FXML
	public void printerAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrinterSelect.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Event Listener on JFXButton[#btnMinus].onAction
	@FXML
	public void onMinus(ActionEvent event) {
		((Stage) ((Button)event.getSource()).getScene().getWindow()).setIconified(true);
	}
	// Event Listener on JFXButton[#btnSquare].onAction
	@FXML
	public void onSquare(ActionEvent event) {
		if(isMax)
		{
			Stage stage = ((Stage) ((Button)event.getSource()).getScene().getWindow());
			stage.setMaximized(false);
			stage.setMinWidth(width);
			stage.setMinHeight(height);

		}
		else
		{
			Scene scene = btnCustomer.getScene();
			width = scene.getWidth();
			height = scene.getHeight();
			((Stage) ((Button)event.getSource()).getScene().getWindow()).setMaximized(true);

		}	
		isMax = !isMax;
	}
	// Event Listener on JFXButton[#btnCross].onAction
	@FXML
	public void onCloss(ActionEvent event) {
		if(Dialog.confirmAlert(null, "Are you Sure! you want to Close")){
			LoginHistory loginHistory = new LoginHistory();
			loginHistory.setDate(new Date());
			loginHistory.setTime(new Date());
			loginHistory.setEmployee(LoginScreenController.emp);
			loginHistory.setStatus("Logout Successfully");
			new DataBase().create(loginHistory);
			System.exit(0);
		}
	}

	public void sendReference(boolean sales, boolean customer, boolean doctor,
			boolean employee, boolean supplier, boolean purchase,
			boolean inventory, boolean reports, boolean prescription,
			boolean configuration,Stage stage) {
		btnSales.setDisable(sales);
		btnCustomer.setDisable(customer);
		btnDoctor.setDisable(doctor);
		btnEmployee.setDisable(employee);
		btnSupplier.setDisable(supplier);
		btnPurchase.setDisable(purchase);
		btnInventory.setDisable(inventory);
		btnReport.setDisable(reports);
		btnPrescription.setDisable(prescription);
		btnConfiguration.setDisable(configuration);
		this.stage = stage;

	}

	private void showDashboard()
	{
		try {
			stt="/views/Dashboard.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(stt));
			Parent root = loader.load();
			mainBorder.setCenter(root);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void buttonFocus(Button btn)
	{
		HBox box = (HBox) btn.getParent();
		label = new Label();
		label.setPrefHeight(50);
		label.setPrefWidth(8);
		label.setBackground(new Background( new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		box.getChildren().add(label);
		selectedBox = box;
	}

	private void changeTheme(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainWindow.fxml"));
			Parent root = fxmlLoader.load();
			MainWindowController mwc = (fxmlLoader).getController();
			if(LoginScreenController.emp.getEmpDesignation().equals("Admin")) {
				mwc.sendReference(false, false, false, false, false, false, false, false, false,false,primaryStage);
			}
			else if(LoginScreenController.emp.getEmpDesignation().equals("General Manager")) {
				mwc.sendReference(false, false, false, true, false, false, false, true, false,true,primaryStage);
			}
			else if(LoginScreenController.emp.getEmpDesignation().equals("Sales Manager")) {
				mwc.sendReference(false, false, false, true, true, true, true, true, false,true,primaryStage);
			}
			else if(LoginScreenController.emp.getEmpDesignation().equals("Stock Manager")) {
				mwc.sendReference(true, true, true, true, false, false, false, true, true,true,primaryStage);
			}
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application"+LoginScreenController.theme.getThemeId()+".css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/media/logo.png"));
			primaryStage.setTitle("Home");


			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if (event.getX() > primaryStage.getWidth() - 15
							&& event.getX() < primaryStage.getWidth() + 15
							&& event.getY() > primaryStage.getHeight() - 15
							&& event.getY() < primaryStage.getHeight() + 15) {
						resizebottom = true;
						//	dx = primaryStage.getWidth() - event.getX();
						//		dy = primaryStage.getHeight() - event.getY();
					} else {
						resizebottom = false;
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
					}
				}
			});

			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if (resizebottom == false) {
						//		primaryStage.setX(event.getScreenX() - xOffset);
						//			primaryStage.setY(event.getScreenY() - yOffset);
					} else {
						primaryStage.setWidth(event.getX() + dx);
						primaryStage.setHeight(event.getY() + dy);

						new Thread(new Runnable() {

							@Override
							public void run() {
								if(primaryStage.getWidth() <= width)
								{
									primaryStage.setWidth(width);
								}
								else if(primaryStage.getHeight() <= height)
								{
									primaryStage.setHeight(height);
								}

								if(primaryStage.getWidth() <= width && primaryStage.getHeight() <= height ) {
									primaryStage.setWidth(width);
									primaryStage.setHeight(height);
								}
							}
						}).start();

					}
				}
			});

			primaryStage.initStyle(StageStyle.UNDECORATED);

			primaryStage.setMinWidth(width);

			primaryStage.setMinHeight(height);
			Util.hideWindow(event);
			primaryStage.show();


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
