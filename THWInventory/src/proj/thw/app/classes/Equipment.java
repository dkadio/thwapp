package proj.thw.app.classes;

import java.io.Serializable;

public class Equipment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String equipNo;
	private String invNo;
	private String deviceNo;
	private String description;
	private String type;
	private int targetQuantity;
	private int actualQuantity;
	private int stock;
	private String status;
	private int layer;
	

	public String getEquipNo() {
		return equipNo;
	}
	public void setEquipNo(String equipNo) {
		this.equipNo = equipNo;
	}
	public String getInvNo() {
		return invNo;
	}
	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTargetQuantity() {
		return targetQuantity;
	}
	public void setTargetQuantity(int targetQuantity) {
		this.targetQuantity = targetQuantity;
	}
	public int getActualQuantity() {
		return actualQuantity;
	}
	public void setActualQuantity(int actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public String toString()
	{
		return this.getType() + " " +this.getEquipNo();
	}
}
