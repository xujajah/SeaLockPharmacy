package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Theme;
import utils.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import database.DBUtil;
import database.DataBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ThemeController implements Initializable{
	@FXML
	private HBox barColor;
	private DataBase dbTheme;

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
    
    private double width = 650;
	private double height = 1100;
	double xOffset ;
	double yOffset ;
	Boolean resizebottom = false;
	double dx;
	double dy;

    @FXML
    void ThemeAction(ActionEvent event) {
    	dbTheme = new DataBase();		
		Theme t = new Theme();
		t.setId(2);
		t= (Theme) DBUtil.getObject(Theme.class, 2);
		
    	JFXButton button= (JFXButton) event.getSource();
    	
    	switch(button.getId()) {
    	case "theme1":
			LoginScreenController.theme.setThemeId(1);
			dbTheme.update(LoginScreenController.theme);

			break;
		case "theme2":
			LoginScreenController.theme.setThemeId(2);
			dbTheme.update(LoginScreenController.theme);

			break;
		case "theme3":
			LoginScreenController.theme.setThemeId(3);
			dbTheme.update(LoginScreenController.theme);

			break;
		case "theme4":
			LoginScreenController.theme.setThemeId(4);
			dbTheme.update(LoginScreenController.theme);

			break;
		case "theme5":
			LoginScreenController.theme.setThemeId(5);
			dbTheme.update(LoginScreenController.theme);
			break;
		case "theme6":
			LoginScreenController.theme.setThemeId(6);
			dbTheme.update(LoginScreenController.theme);

			break;
		case "theme7":
			LoginScreenController.theme.setThemeId(7);
			dbTheme.update(LoginScreenController.theme);
			break;
    	}//switch
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	
}
