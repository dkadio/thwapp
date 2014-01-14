package proj.thw.app.classes;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

@DatabaseTable(tableName = "equipmentimage")
public class EquipmentImage implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId=true)
	private int id;

	@DatabaseField(dataType=DataType.BYTE_ARRAY)
	private byte[] imgBytes;
	
	private Bitmap img;
	
	public EquipmentImage()
	{
		//DB-Constructor
	}
	
	public EquipmentImage(Bitmap img) throws Exception
	{
		this.img = img;
		imgBytes = imgToBytes();
	}
	
	private byte[] imgToBytes() throws Exception
	{
		if(img != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
		else
		{
			throw new Exception("Image not init!");
		}
	}
	
	private Bitmap bytesToImage() throws Exception
	{
		if(imgBytes != null)
		{
			return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
		}
		else
		{
			throw new Exception("ImageBytes not init!");
		}
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
			try {
				img = bytesToImage();
			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
		}
		
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	public byte[] getImgBytes() {
		return imgBytes;
	}

	public void setImgBytes(byte[] imgBytes) {
		this.imgBytes = imgBytes;
		try {
			img = bytesToImage();
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}
	
}
