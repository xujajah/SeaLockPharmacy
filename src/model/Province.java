package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Province {
	private IntegerProperty provinceId = new SimpleIntegerProperty(this,"provinceId",0);
	private StringProperty provinceName = new SimpleStringProperty(this,"provinceName","");
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getProvinceId() {
		return provinceId.get();
	}
	@NotNull
	@Column(unique = true)
	public String getProvinceName() {
		return provinceName.get();
	}
	
	public void setProvinceId(int provinceId) {
		this.provinceId.set(provinceId);
	}
	public void setProvinceName(String provinceName) {
		this.provinceName.set(provinceName);
	}
	
	public IntegerProperty provinceIdProperty() {
		return provinceId;
	}
	public StringProperty provinceNameProperty() {
		return provinceName;
	}
	
	@Override
	public String toString() {
		return getProvinceName();
	}

}
