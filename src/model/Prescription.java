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
import javax.validation.constraints.NotNull;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Prescription {
	private IntegerProperty prescriptionId = new SimpleIntegerProperty(this,"prescriptionId",0);
	private StringProperty prescriptionDisease = new SimpleStringProperty(this, "prescriptionDisease", "");
	private ObjectProperty<Date> prescriptionDate = new SimpleObjectProperty<Date>(this, "prescriptionDate", new Date());
	private ObjectProperty<Customer> prescriptionCustomer = new SimpleObjectProperty<Customer>(this,"prescriptionCustomer",new Customer());
	private ObjectProperty<Doctor> prescriptionDoctor = new SimpleObjectProperty<Doctor>(this,"prescriptionDoctor",new Doctor());
	private ObjectProperty<Employee> prescriptionEmployee = new SimpleObjectProperty<Employee>(this,"prescriptionEmployee",new Employee());

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getPrescriptionId() {
		return prescriptionId.get();
	}

	@NotNull
	public String getPrescriptionDisease() {
		return prescriptionDisease.get();
	}

	@NotNull
	@Temporal (TemporalType.DATE)
	public Date getPrescriptionDate() {
		return prescriptionDate.get();
	}

	@ManyToOne
	@JoinColumn(name = "prescriptionCustomer")
	public Customer getPrescriptionCustomer() {
		return prescriptionCustomer.get();
	}

	@ManyToOne
	@JoinColumn(name = "prescriptionDoctor")
	public Doctor getPrescriptionDoctor() {
		return prescriptionDoctor.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "prescriptionEmployee")
	public Employee getPrescriptionEmployee() {
		return prescriptionEmployee.get();
	}

	//get Property
	public IntegerProperty prescriptionIdProperty() {
		return prescriptionId;
	}
	public StringProperty prescriptionDiseaseProperty() {
		return prescriptionDisease;
	}
	public ObjectProperty<Date> prescriptionDateProperty() {
		return prescriptionDate;
	}
	public ObjectProperty<Customer> prescriptionCustomerProperty() {
		return prescriptionCustomer;
	}
	public ObjectProperty<Doctor> prescriptionDoctorProperty() {
		return prescriptionDoctor;
	}
	public ObjectProperty<Employee> prescriptionEmployeeProperty() {
		return prescriptionEmployee;
	}

	//setters
	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId.set(prescriptionId);
	}
	public void setPrescriptionDisease(String prescriptionDisease) {
		this.prescriptionDisease.set(prescriptionDisease);
	}
	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate.set(prescriptionDate);
	}
	public void setPrescriptionCustomer(Customer prescriptionCustomer) {
		this.prescriptionCustomer.set(prescriptionCustomer);
	}
	public void setPrescriptionDoctor(Doctor prescriptionDoctor) {
		this.prescriptionDoctor.set(prescriptionDoctor);
	}
	public void setPrescriptionEmployee(Employee prescriptionEmployee) {
		this.prescriptionEmployee.set(prescriptionEmployee);
	}

	@Override
	public String toString() {
		return getPrescriptionDisease() + " "+getPrescriptionDate()+" Dr. "+getPrescriptionDoctor().getDocName()  ;
	}



}
