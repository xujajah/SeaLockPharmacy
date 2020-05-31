package controller;


import java.io.IOException;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import database.DBUtil;
import database.DataBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;
import model.LoginHistory;
import model.Theme;
import utils.Dialog;
import utils.Util;

public class LoginScreenController implements Initializable{

	@FXML
	private BorderPane mainBorder;

	@FXML
	private JFXTextField txtUsername;


	@FXML
	private JFXPasswordField txtPassword;

	@FXML
	private JFXButton btnLogin;

	@FXML
	private JFXButton btnCancel;
	public static Employee emp;
	public static Theme theme;

	double xOffset ;
	double yOffset ;
	Boolean resizebottom = false;
	double dx;
	double dy;
	int height=650;
	int width=1100;
	DataBase dbTheme;

	@FXML
	public void cancelAction(ActionEvent event) {
		if(Dialog.confirmAlert(null,"Are you sure you want to cancel")) {
			Util.hideWindow(event);
		}
	}

	@FXML
	public void loginAction(ActionEvent event) {
		if( Util.isValid(txtUsername)&&
		Util.isValid(txtPassword)) {
			emp = (Employee) DBUtil.isLogin(Employee.class, "empUserName", txtUsername.getText(), "empPswd", txtPassword.getText());
			if(emp!=null) {
				try {

					theme = DBUtil.getTheme(emp);
					LoginHistory loginHistory = new LoginHistory();
					loginHistory.setDate(new Date());
					loginHistory.setTime(new Date());
					loginHistory.setEmployee(emp);
					loginHistory.setStatus("Login Successfully");
					new DataBase().create(loginHistory);
					Util.hideWindow(event);
					Stage primaryStage = new Stage();
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainWindow.fxml"));
					Parent root = fxmlLoader.load();
					MainWindowController mwc = (fxmlLoader).getController();
					if(emp.getEmpDesignation().equals("Admin")) {
						mwc.sendReference(false, false, false, false, false, false, false, false, false,false,primaryStage);
					}
					else if(emp.getEmpDesignation().equals("General Manager")) {
						mwc.sendReference(false, false, false, true, false, false, false, true, false,true,primaryStage);
					}
					else if(emp.getEmpDesignation().equals("Sales Manager")) {
						mwc.sendReference(false, false, false, true, true, true, true, true, false,true,primaryStage);
					}
					else if(emp.getEmpDesignation().equals("Stock Manager")) {
						mwc.sendReference(true, true, true, true, false, false, false, true, true,true,primaryStage);
					}
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/application/application"+theme.getThemeId()+".css").toExternalForm());
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

					primaryStage.show();	

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				Dialog.error("Invaild UserName or Password");
				Employee employee = (Employee) DBUtil.getObject(Employee.class, "empUserName", txtUsername.getText());
				if(employee!=null) {
					LoginHistory loginHistory = new LoginHistory();
					loginHistory.setDate(new Date());
					loginHistory.setTime(new Date());
					loginHistory.setEmployee(employee);
					loginHistory.setStatus("Unsuccessfull");
					new DataBase().create(loginHistory);
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
