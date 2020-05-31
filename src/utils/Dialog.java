package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

public class Dialog {
	
    public static boolean confirmAlert(Window stage, String mess) {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
		alert.initModality(Modality.APPLICATION_MODAL);
		if(stage!= null)
		{
			alert.initOwner(stage);
		}
		alert.setContentText(mess);
		Button okButton = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
	    okButton.setText("Yes");
	    Button cancelButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
	    cancelButton.setText("No");
			
		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void exception(Exception e){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception");
		alert.setHeaderText("There is an Exception type");
		alert.setContentText(e.toString());
		alert.showAndWait();
	}
	
	public static void error(String info){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("There is an Error");
		alert.setContentText(info);
		alert.showAndWait();
	}
	
	public static void information(String info){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(info);
		alert.showAndWait();
	}
	public static void confirmation(String info){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText(info);
		alert.showAndWait();
	}
	public static void warning(String info){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(info);
		alert.showAndWait();
	}
}
