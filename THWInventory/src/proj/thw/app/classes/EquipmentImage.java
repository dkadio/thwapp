package proj.thw.app.classes;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Klasse, die ein EquipmentImage dartsellt. Diese Klasse dient dazu, das an dem Equipment ein Image haengt und nicht nur
 * das Byte-Array, welches als Blobb gespeichert wird..
 * @author max / deniz
 *
 */
//@DatabaseTable(tableName = "equipmentimage")
public class EquipmentImage implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//@DatabaseField(generatedId=true)
	private int id;

	@DatabaseField(dataType=DataType.BYTE_ARRAY)
	private byte[] imgBytes;
	
	transient private Bitmap img;
	
	public EquipmentImage()
	{
		//DB-Constructor
	}
	
	public EquipmentImage(Bitmap img)
	{
		this.img = img;
		imgBytes = imgToBytes();
	}
	
	private byte[] imgToBytes()
	{
		if(img != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
		return null;
	}
	
	private Bitmap bytesToImage()
	{
		if(imgBytes != null)
		{
			return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);	
		}
		
		return null;
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Bitmap getImg() {
		
		if(img == null)
		{
			img = bytesToImage();
		}
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
		this.imgBytes = imgToBytes();
	}

	public byte[] getImgBytes() {
		return imgBytes;
	}

	public void setImgBytes(byte[] imgBytes) {
		this.imgBytes = imgBytes;
		img = bytesToImage();
	}
	
}
