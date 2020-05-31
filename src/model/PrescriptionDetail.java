package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@Entity
public class PrescriptionDetail {
	private IntegerProperty prescriptionDetailId = new SimpleIntegerProperty(this,"prescriptionDetailId",0);
	private DoubleProperty prescriptionDetailDosage = new SimpleDoubleProperty(this,"prescriptionDetailDosage",0.0);
	private ObjectProperty<Stock> prescriptionDetailDrug = new SimpleObjectProperty<Stock>(this,"prescriptionDetailDrug",new Stock());
	private ObjectProperty<Prescription> prescriptionDetailPrescription = new SimpleObjectProperty<Prescription>(this,"prescriptionDetailPrescription",new Prescription());
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public int getPrescriptionDetailId() {
		return prescriptionDetailId.get();
	}
	
	@NotNull
	public double getPrescriptionDetailDosage() {
		return prescriptionDetailDosage.get();
	}
	
	@ManyToOne
	@JoinColumn (name = "prescriptionDrug")
	public Stock getPrescriptionDetailDrug() {
		return prescriptionDetailDrug.get();
	}
	
	@ManyToOne
	@JoinColumn (name ="Prescription")
	public Prescription getPrescriptionDetailPrescription() {
		return prescriptionDetailPrescription.get();
	}
	
	//get Property
	public IntegerProperty prescriptionDetailIdProperty() {
		return prescriptionDetailId;
	}
	public DoubleProperty prescriptionDetailDosageProperty() {
		return prescriptionDetailDosage;
	}
	public ObjectProperty<Stock> prescriptionDetailDrugProperty() {
		return prescriptionDetailDrug;
	}
	public ObjectProperty<Prescription> prescriptionDetailPrescriptionProperty() {
		return prescriptionDetailPrescription;
	}
	
	//setters
	public void setPrescriptionDetailId(int prescriptionDetailId) {
		this.prescriptionDetailId.set(prescriptionDetailId);
	}
	public void setPrescriptionDetailDosage(double prescriptionDetailDosage) {
		this.prescriptionDetailDosage.set(prescriptionDetailDosage);
	}
	public void setPrescriptionDetailDrug(Stock prescriptionDetailDrug) {
		this.prescriptionDetailDrug.set(prescriptionDetailDrug);
	}
	public void setPrescriptionDetailPrescription(Prescription prescriptionDetailPrescription) {
		this.prescriptionDetailPrescription.set(prescriptionDetailPrescription);
	}
	
	
	

}
