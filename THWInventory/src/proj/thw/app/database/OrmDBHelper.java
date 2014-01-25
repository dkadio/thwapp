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
	private ConnectionSource src;

	public OrmDBHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
		try {
			dbHelperEquip = new DBHelperEquip(createEquipmentDAO());
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource src) {
		
		try {
			this.src = src;
			createTables(src);
			
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
	
	private void createTables(ConnectionSource src) throws SQLException
	{
		//TableUtils.createTable(src, EquipmentImage.class);
		TableUtils.createTable(src, Equipment.class);
	}
	
	private void dropTables(ConnectionSource src) throws SQLException
	{
		TableUtils.dropTable(src, EquipmentImage.class, true);
		TableUtils.dropTable(src, Equipment.class, true);
	}
	
	public void clearDB() throws SQLException
	{
		//TableUtils.clearTable(getConnectionSource(), EquipmentImage.class);
		TableUtils.clearTable(getConnectionSource(), Equipment.class);
	}
}
