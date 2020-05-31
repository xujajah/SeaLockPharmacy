package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import database.DBUtil;
import database.DataBase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.Customer;
import model.Employee;
import model.PosPrinter;
import model.Province;
import model.Region;
import model.Theme;
import utils.Dialog;

public class SplashController implements Initializable{

	@FXML
	private BorderPane pane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new SplashScreen().start();
	}

	public class SplashScreen extends Thread{


		Theme t= new Theme();

		public void run(){
			try {
				Thread.sleep(2000);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						try {

							database.DBData.createSessionFactory();
							if(!DBUtil.dataInTable(Company.class)) {
								Company company = new Company();
								company.setName("Eagale Pharmacy");
								company.setAddress("Saddique Center");
								company.setCity("Rahwali");
								company.setPhone("03000451616  03000561616");
								company.setBillNotes("Store Medicines In Cool And Dry Place");
								DataBase db =new DataBase();
								db.create(company);
							}
							Company company = (Company) DBUtil.getObject(Company.class, 1);
							if(company!=null) {
								if(!DBUtil.dataInTable(Employee.class)) {
									Employee emp = new Employee();
									emp.setEmpName("Admin");
									emp.setEmpIdentity("Admin");
									emp.setEmpAddress("Admin");
									emp.setEmpDesignation("Admin");
									emp.setEmpCNIC("Admin");
									emp.setEmpPhone("Admin");
									emp.setEmpUserName("admin");
									emp.setEmpPswd("admin");
									emp.setEmpEmail("Admin");
									emp.setEmpDOB(new Date());
									emp.setEmpActive(true);
									DataBase db = new DataBase();
									db.create(emp);

									Theme theme = new Theme();
									theme.setThemeId(1);
									theme.setEmployeeTheme(emp);
									new DataBase().create(theme);
								}
								if(!DBUtil.dataInTable(Customer.class)) {
									Customer customer = new Customer();
									Employee emp = (Employee) DBUtil.getObject(Employee.class, 1);
									customer.setCustName("Walk In Customer");
									customer.setCustIdentity("Walk In Customer");
									customer.setCustAddress("Walk In Customer");

									Province province = new Province();
									province.setProvinceName("Punjab");
									new DataBase().create(province);

									Region region = new Region();
									region.setRegionName("Lahore");
									region.setRegionProvince(province);
									new DataBase().create(region);

									customer.setCustRegion(region);
									customer.setCustDOB(new Date());
									customer.setCustPhone("Walk In Customer");
									customer.setCustEmail("Walk In Customer");
									customer.setCustEmployee(emp);
									customer.setCustActive(true);
									DataBase db =new DataBase();
									db.create(customer);
								}
								if(!DBUtil.dataInTable(PosPrinter.class)) {
									PosPrinter posPrinter = new PosPrinter();
									Printer defaultprinter = Printer.getDefaultPrinter();
									if (defaultprinter != null) 
									{
										posPrinter.setName(defaultprinter.getName());
									} 
									else 
									{
										posPrinter.setName("No printers installed.");
									}
									new DataBase().create(posPrinter);
								}

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
									pane.getScene().getWindow().hide();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else {
								Dialog.error("No Database Connection Found..!!");
								System.exit(0);
							}

						} catch (Exception e) {
							e.printStackTrace();
							Dialog.error("No Database Connection Found..!!");
							System.exit(0);
						}	
					}
				});
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

}
