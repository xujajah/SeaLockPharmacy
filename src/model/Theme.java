package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@Entity
public class Theme {
	private IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
	private IntegerProperty themeId= new SimpleIntegerProperty(this,"themeId",0);
	private ObjectProperty<Employee> employeeTheme = new SimpleObjectProperty<Employee>(this,"employeeTheme",new Employee());
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getId() {
		return id.get();
	}
	public int getThemeId() {
		return themeId.get();
	}
	
	@OneToOne
	@JoinColumn(name = "employeeTheme")
	public Employee getEmployeeTheme() {
		return employeeTheme.get();
	}
	
	//Property
	public IntegerProperty idProperty() {
		return id;
	}
	public IntegerProperty themeIdProperty() {
		return themeId;
	}
	public ObjectProperty<Employee> employeeThemeProperty() {
		return employeeTheme;
	}
	
	//setters
	public void setId(int id) {
		this.id.set(id);
	}
	public void setThemeId(int themeId) {
		this.themeId.set(themeId);
	}
	public void setEmployeeTheme(Employee employeeTheme) {
		this.employeeTheme.set(employeeTheme);
	}
	
	

//	public IntegerProperty themeIdProperty() {
//		return themeId;
//	}
//
//	public int getThemeid() {
//		return themeId.get();
//	}
//	
//	public void setThemeid(int themeId) {
//		this.themeId.set(themeId);
//	}
//
//	@Id
//	public int getId() {
//		return id.get();
//	}
//	
//	public IntegerProperty idProperty() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id.set(id);
//	}
	

}
