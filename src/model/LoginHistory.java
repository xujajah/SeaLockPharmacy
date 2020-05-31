package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class LoginHistory {
	private IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
	private ObjectProperty<Employee> employee = new SimpleObjectProperty<Employee>(this,"employee",new Employee());
	private StringProperty status = new SimpleStringProperty(this,"status","");
	private ObjectProperty<Date> date = new SimpleObjectProperty<Date>(this,"date",new Date());
	private ObjectProperty<Date> time = new SimpleObjectProperty<Date>(this,"time",new Date());
	
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	public int getId() {
		return id.get();
	}
	@ManyToOne
	@JoinColumn(name = "employee")
	public Employee getEmployee() {
		return employee.get();
	}
	public String getStatus() {
		return status.get();
	}
	
	@Temporal(value = TemporalType.DATE)
	public Date getDate() {
		return date.get();
	}
	
	@Temporal(value = TemporalType.TIME)
	public Date getTime() {
		return time.get();
	}
	
	
	//property
	public IntegerProperty idProperty() {
		return id;
	}
	public ObjectProperty<Employee> employeeProperty() {
		return employee;
	}
	public StringProperty statusProperty() {
		return status;
	}
	public ObjectProperty<Date> dateProperty() {
		return date;
	}
	public ObjectProperty<Date> timeProperty() {
		return time;
	}
	
	//setters
	public void setId(int id) {
		this.id.set(id);
	}
	public void setEmployee(Employee employee) {
		this.employee.set(employee);
	}
	public void setStatus(String status) {
		this.status.set(status);
	}
	public void setDate(Date date) {
		this.date.set(date);
	}
	public void setTime(Date time) {
		this.time.set(time);
	}
	

}
