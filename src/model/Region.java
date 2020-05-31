package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Region {
	private IntegerProperty regionId = new SimpleIntegerProperty(this,"regionId",0);
	private StringProperty regionName = new SimpleStringProperty(this,"regionName","");
	private ObjectProperty<Province> regionProvince = new SimpleObjectProperty<Province>(this,"regionProvince",new Province());
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getRegionId() {
		return regionId.get();
	}
	
	@NotNull
	@Column(unique = true)
	public String getRegionName() {
		return regionName.get();
	}
	
	@ManyToOne
	@JoinColumn(name = "regionProvince")
	public Province getRegionProvince() {
		return regionProvince.get();
	}
	
	public void setRegionId(int regionId) {
		this.regionId.set(regionId);
	}
	public void setRegionName(String regionName) {
		this.regionName.set(regionName);
	}
	public void setRegionProvince(Province regionProvince) {
		this.regionProvince.set(regionProvince);
	}
	
	public IntegerProperty regionIdProperty() {
		return regionId;
	}
	public StringProperty regionNameProperty() {
		return regionName;
	}
	public ObjectProperty<Province> regionProvinceProperty(){
		return regionProvince;
	}
	
	@Override
	public String toString() {
		return getRegionName();
	}

}
