package proj.thw.app.classes;

import java.io.Serializable;
import java.util.Vector;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "equipment")
public class Equipment implements Serializable {
	
	public static enum Type{POS,SATZ,GWM,TEIL,NOTYPE};
	public static enum Status {V,A,F,BA,NOSTATUS};

	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId=true)
	private int 			id;
	
	@DatabaseField
	private int 			layer;
	
	@DatabaseField
	private String 			location;
	
	@DatabaseField
	private String 			invNo;
	
	@DatabaseField
	private String 			equipNo;
	
	@DatabaseField
	private String 			deviceNo;
	
	@DatabaseField
	private String 			description;
	
	@DatabaseField
	private int 			targetQuantity;
	
	@DatabaseField
	private int 			actualQuantity;
	
	@DatabaseField
	private int 			stock;
	
	@DatabaseField
	private boolean 		foreignPart;
	
	@DatabaseField(dataType=DataType.ENUM_STRING)
	private Type 			type;
	
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private Vector<Status> 	status;
	
	@DatabaseField(foreign=true)
	EquipmentImage 			equipImg; 
	
	public Equipment()
	{
		status = new Vector<Status>();
		equipImg = new EquipmentImage();
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EquipmentImage getEquipImg() {
		return equipImg;
	}

	public void setEquipImg(EquipmentImage equipImg) {
		this.equipImg = equipImg;
	}
	
	public String toString()
	{
		return equipNo + " ("+status.toString()+") ";
	}

}
