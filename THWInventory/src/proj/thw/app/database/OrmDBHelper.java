package proj.thw.app.database;

import java.sql.SQLException;

import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.EquipmentImage;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class OrmDBHelper extends OrmLiteSqliteOpenHelper {

	public static final String LOG 			= OrmDBHelper.class.getName();
	public static final String DB_NAME 		= "thwinventory.db";
	public static final int DB_VERSION 		= 1;
	
	private DBHelperEquip dbHelperEquip;

	public OrmDBHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource src) {
		
		try {
			TableUtils.createTable(src, EquipmentImage.class);
			TableUtils.createTable(src, Equipment.class);
			
			dbHelperEquip = new DBHelperEquip(createEquipmentDAO());
		} catch (SQLException e) {
			Log.e(LOG, e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {

	}
	
	public Dao<Equipment,Integer> createEquipmentDAO() throws SQLException
	{
		return DaoManager.createDao(connectionSource, Equipment.class);
	}
	
	public DBHelperEquip getDbHelperEquip() {
		return dbHelperEquip;
	}
}
