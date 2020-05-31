package application;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/Splash.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());	

			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/media/logo.png"));
			primaryStage.setTitle("SplashScreen");
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();	
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	public static void main(String[] args) {
		launch(args);
	}
}
