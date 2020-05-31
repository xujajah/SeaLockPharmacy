package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UPC {
	@Id
	private int id;
	private int upc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUpc() {
		return upc;
	}
	public void setUpc(int upc) {
		this.upc = upc;
	}
	

}
