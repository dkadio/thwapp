package proj.thw.app.classes;

import java.io.Serializable;
import java.util.Vector;

public class Equipment implements Serializable {
	
	
	
	public enum Type{POS,SATZ,GWM,TEIL,NOTYPE};
	public enum Status {V,F,BA,NOSTATUS};
	
	private static final long serialVersionUID = 1L;
	
	//TODO Id?
	
	private int 			layer;
	private String 			location;
	private String 			invNo;
	private String 			equipNo;
	private String 			deviceNo;
	private String 			description;
	private int 			targetQuantity;
	private int 			actualQuantity;
	private int 			stock;
	private boolean 		foreignPart;
	private Type 			type;
	private Vector<Status> 	status;
	
	public Equipment()
	{

	}
	
	public Equipment(	int layer,String location, String invNo, 
						String equipNo, String deviceNo, String description,
						int targetQuantity, int actualQuantity, int stock, 
						boolean foreignPart, Type type, Vector<Status> status)
	{
			this.layer 			= layer;
			this.location 		= location;
			this.invNo			= invNo;
			this.equipNo		= equipNo;
			this.deviceNo		= deviceNo;
			this.description	= description;
			this.targetQuantity = targetQuantity;
			this.actualQuantity = actualQuantity;
			this.stock			= stock;
			this.foreignPart	= foreignPart;
			this.type			= type;
			this.status			= status;
	}
	
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getInvNo() {
		return invNo;
	}
	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}
	public String getEquipNo() {
		return equipNo;
	}
	public void setEquipNo(String equipNo) {
		this.equipNo = equipNo;
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
	public boolean isForeignPart() {
		return foreignPart;
	}
	public void setForeignPart(boolean foreignPart) {
		this.foreignPart = foreignPart;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Vector<Status> getStatus() {
		return status;
	}
	public void setStatus(Vector<Status> status) {
		this.status = status;
	}
	
	

}
