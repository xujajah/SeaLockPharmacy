package utils;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.hibernate.Transaction;

import database.DBData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.UPC;

public class Util {

	public static void checkName(TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("^[A-Za-z \\s]*")) {
					tf.setText(newValue.replaceAll("[^A-Za-z \\s]", ""));
					showToolTip(tf, "Only Alphabets Allowed");
				}
			}
		});
	}

	public static void checkNumber(TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("^[\\d]*")) {
					tf.setText(newValue.replaceAll("[^\\d]", ""));
					showToolTip(tf, "Enter Numbers Only");
				}
			}
		});
	}

	public static void checkDecimalNumber(TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("^[.\\d]*")) {
					tf.setText(newValue.replaceAll("[^.\\d]", ""));
					showToolTip(tf, "Enter Numbers Only");
				}
			}
		});

		tf.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue) {
					if(!tf.getText().isEmpty()){
						Pattern ValidDecimal = Pattern.compile("(?<=^| )\\d+(\\.\\d+)?(?=$| )|(?<=^| )\\.\\d+(?=$| )");
						Matcher matcher = ValidDecimal.matcher(tf.getText());
						if(!matcher.find() ) {
							showToolTip(tf, "Format should be 0.0");
							tf.requestFocus();
						}
					}
				}
			}
		});
	}

	public static void checkCNIC(TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("^[0-9 -]*")) {
					tf.setText(newValue.replaceAll("[^\\d -]", ""));
					showToolTip(tf, "Enter Numbers Only");
				}
			}
		});

		final int MAX_LENGTH = 15;
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.length() > MAX_LENGTH) {
					String s = tf.getText().substring(0,MAX_LENGTH);
					tf.setText(s);
					showToolTip(tf, "Only "+MAX_LENGTH+" Digits are Allowed");
				}

			}
		});

		tf.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue) {
					if(!tf.getText().isEmpty()){
						Pattern ValidCNIC = 
								Pattern.compile("^[0-9]{5,6}-[0-9]{6,7}-[0-9]{1}$");
						Matcher matcher = ValidCNIC.matcher(tf.getText());
						if(!matcher.find()){
							showToolTip(tf, "Format should be 12345-1234567-1");
							tf.requestFocus();
						}
					}
				}
			}
		});
	}

	public static void numberLimit(TextField tf) {
		final int MAX_LENGTH = 15;
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.length() > MAX_LENGTH) {
					String s = tf.getText().substring(0,MAX_LENGTH);
					tf.setText(s);
					showToolTip(tf, "Only "+MAX_LENGTH+" Digits are Allowed");
				}
			}
		});
	}

	public static void checkPositiveNumber(TextField tf)
	{
		tf.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("^[1-9]\\d*") && !tf.getText().isEmpty()) {
							tf.setText(newValue.replaceAll("[^\\d]", ""));
							if(newValue.equals("0"))
								tf.setText(newValue.replace("0", ""));
							showToolTip(tf,"Enter Integer Value greater than 0");
						}
					}
				});
	}

	public static void emailCheck(TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("^[a-z0-9._@-]*")) {
					tf.setText(newValue.replaceAll("[^a-z0-9._@-]", ""));
					showToolTip(tf, "Format should be abc@gmail.com");
				}
			}
		});

		tf.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue) {
					if(!tf.getText().isEmpty()){
						Pattern VALID_EMAIL_ADDRESS_REGEX = 
								Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");
						Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(tf.getText());
						if(!matcher.find()){
							showToolTip(tf, "Format should be abc@gmail.com");
							tf.requestFocus();
						}
					}
				}
			}
		});

	}

	public static void showToolTip(Node node , String message) {
		Tooltip tp = new Tooltip(message);
		Bounds bounds = node.localToScreen(node.getBoundsInLocal());
		if(bounds != null) {
			tp.show(node,bounds.getMinX()+bounds.getWidth()/2,bounds.getMinY()+bounds.getHeight()+4);
			tp.setAutoHide(true);

			Task<Void> sleeper = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(2000);
					}catch (InterruptedException e) {					
					}
					return null;
				}
			};
			sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent arg0) {
					// TODO Auto-generated method stub
					tp.hide();
				}
			});
			new Thread(sleeper).start();
		}
	}

	public static Boolean isValid(Node node)
	{

		if(node.getClass().getSimpleName().equals("JFXComboBox")||node.getClass().getSimpleName().equals("ComboBox"))
		{
			ComboBox<?> box = ((ComboBox<?>)node);
			if(box.getSelectionModel().getSelectedItem() == null || box.getSelectionModel().getSelectedItem().toString().isEmpty())
			{
				showToolTip(node);
				node.requestFocus();
				return false;
			}
		}
		else if(node.getClass().getSimpleName().equals("TextArea"))
		{
			if (((TextArea) node).getText().isEmpty()) {
				showToolTip(node);
				node.requestFocus();
				return false;
			}
		}
		else if(node.getClass().getSimpleName().equals("JFXDatePicker"))
		{
			//			if (((DatePicker) node).getValue().toString().isEmpty() || ((DatePicker) node).getValue().toString() == null ) {
			if (((DatePicker) node).getValue()==null   ) {
				showToolTip(node);
				node.requestFocus();
				return false;
			}
		}
		else if (((TextField) node).getText().isEmpty()) {
			showToolTip(node);
			node.requestFocus();
			return false;
		}

		return true;
	}

	public static void showToolTip(Node node)
	{
		Tooltip tp;
		//System.out.println(node.getClass().getSimpleName());
		if(node.getClass().getSimpleName().equals("ComboBox"))
			tp = new Tooltip("Select an Item");
		else
			tp = new Tooltip("Fill this Field in correct format.");

		//tp.setStyle("fx-font-size : 24px");
		Bounds bounds = node.localToScreen(node.getBoundsInLocal());

		tp.show(node,bounds.getMinX()+bounds.getWidth()/2,bounds.getMinY()+bounds.getHeight()+4);
		tp.setAutoHide(true);
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				tp.hide();
			}
		});
		new Thread(sleeper).start();
	}

	public static void hideWindow(ActionEvent event)
	{
		((Button)event.getSource()).getScene().getWindow().hide();
	}

	public static void showNotification(Node node , String message) {
		Tooltip tp = new Tooltip(message);
		Bounds bounds = node.localToScreen(node.getBoundsInLocal());
		if(bounds != null) {
			tp.setMaxWidth(Double.MAX_VALUE);
			tp.setMaxHeight(70);
			tp.setFont(Font.font(18));
			tp.show(node,bounds.getMinX(),bounds.getMinY());
			tp.setAutoHide(true);

			Task<Void> sleeper = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					try {
						Thread.sleep(3500);
					}catch (InterruptedException e) {					
					}
					return null;
				}
			};
			sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent arg0) {
					// TODO Auto-generated method stub
					tp.hide();
				}
			});
			new Thread(sleeper).start();
		}
	}

	public static void showNotificationMain(Node node , String message) {
		Tooltip tp = new Tooltip(message);
		Bounds bounds = node.localToScreen(node.getBoundsInLocal());
		if(bounds != null) {
			tp.setMaxWidth(Double.MAX_VALUE);
			tp.setMaxHeight(70);
			tp.setFont(Font.font(18));
			tp.show(node,bounds.getMinX()+170,bounds.getMinY()+100);
			tp.setAutoHide(true);

			Task<Void> sleeper = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(3500);
					}catch (InterruptedException e) {					
					}
					return null;
				}
			};
			sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent arg0) {
					// TODO Auto-generated method stub
					tp.hide();
				}
			});
			new Thread(sleeper).start();
		}
	}

	public static int checkSum (String Input) {
		int evens = 0; //initialize evens variable
		int odds = 0; //initialize odds variable
		int checkSum = 0; //initialize the checkSum
		for (int i = 0; i < Input.length(); i++) {
			//check if number is odd or even
			if (i % 2 ==0){ // check that the character at position "i" is divisible by 2 which means it's even
				evens = evens+( Character.getNumericValue(Input.charAt(i)) * 1);// then add it to the evens
			} else {
				odds =odds + ( Character.getNumericValue(Input.charAt(i)) * 3); // else add it to the odds
			}
		}
		//odds = odds * 3; //multiply odds by three
		int total =  evens + odds; //sum odds and evens
		if (total % 10 == 0){ //if total is divisible by ten, special case
			checkSum = 0;//checksum is zero
		} else { //total is not divisible by ten
			checkSum = 10 - (total % 10); //subtract the ones digit from 10 to find the checksum
		}
		return checkSum;
	}

	public static void  generateUPC(KeyEvent event, TextField textField){
		KeyCombination f2 = new KeyCodeCombination(KeyCode.F2);
		Session session = null;
		Transaction tx = null;
		int result = 0;
		if(f2.match(event)){
			try {
				session = DBData.getSession();
				tx = session.beginTransaction();
				if(session.createCriteria(UPC.class).list().isEmpty()) {
					UPC code = new UPC();
					code.setId(1);
					code.setUpc(1000000);
					session.save(code);
				}
				UPC upc = (UPC) session.get(UPC.class, 1);
				result = result+upc.getUpc()+1;
				upc.setUpc(result);
				tx.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
				if(tx!=null)
					tx.rollback();
			} finally {
				if(session != null)
					session.close();
			}
			String bar = "12345"+result;
			bar = bar + checkSum(bar);
			textField.setText(bar);
		}
	}

	public static Date localDateToJavaUtilDate(LocalDate value) {
		Date date = Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}

	public static Date localDateToJavaSqlDate(LocalDate value) {
		Date date = java.sql.Date.valueOf(value);
		return date;
	}

	public static LocalDate utilDateToLocalDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
				.toLocalDate();
	}

	public static void hideWindow(KeyEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	public static double decimalTwoDigitsDouble(double value) {
		//		DecimalFormat df2 = new DecimalFormat("#.##");
		DecimalFormat df2 = new DecimalFormat("0.000");
		return Double.valueOf(df2.format(value));
	}

	public static String decimalTwoDigitsString(double value) {
		//		DecimalFormat df2 = new DecimalFormat("#.##");
		DecimalFormat df2 = new DecimalFormat("0.00");
		return df2.format(value);
	}

	public static void setDatePicker(DatePicker pickerFrom, DatePicker pickerTo)
	{
		String pattern = "yyyy-MM-dd";
		pickerFrom.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override 
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override 
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		pickerTo.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override 
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override 
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		Callback<DatePicker, DateCell> toDateCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);
				long p = ChronoUnit.DAYS.between(pickerFrom.getValue(), item);
				setTooltip(new Tooltip("You're about to View Reports for " + p + " days"));

				if(item.isBefore(pickerFrom.getValue()) || item.isAfter(LocalDate.now()))
				{
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);

					/* When Hijri Dates are shown, setDisable() doesn't work. Here is a workaround */
					// addEventFilter(MouseEvent.MOUSE_CLICKED, e -> e.consume());
				}
			}
		};

		Callback<DatePicker, DateCell> fromDateCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);
				if(item.isAfter(LocalDate.now()))
				{
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
					
				}
			}
		};

		pickerFrom.setDayCellFactory(fromDateCellFactory);
		pickerTo.setDayCellFactory(toDateCellFactory);
	}

	public static void setDatePicker(DatePicker picker)
	{
		String pattern = "yyyy-MM-dd";
		picker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override 
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override 
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		Callback<DatePicker, DateCell> fromDateCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);

				if(item.isAfter(LocalDate.now()))
				{
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
				}
			}
		};

		picker.setDayCellFactory(fromDateCellFactory);

	}
	
	public static void alertBox(String mes, AlertType type)
	{
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Alert alert = new Alert(type, mes, ButtonType.OK);
				alert.showAndWait();
			}
		});
	}
}
