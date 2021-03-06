package proj.thw.app.database;

import java.sql.SQLException;
import java.util.List;

import proj.thw.app.classes.Equipment;
import com.j256.ormlite.dao.Dao;

/**
 * Klasse, die Datenbankfunktionalitaeten fuer Ein Equipment zur Verfuegung stellt
 * @author max / deniz
 *
 */
public class DBHelperEquip {

	private Dao<Equipment, Integer> equipDao;
	public DBHelperEquip(Dao<Equipment, Integer> equipDao) {
		this.equipDao = equipDao;
	}
	
	public void insertEquipment(Equipment inEquip) throws SQLException 
	{
		equipDao.create(inEquip);
	}
	
	public void deleteEquipment(int id) throws SQLException
	{
		equipDao.deleteById(id);
	}
	
	public void deleteEquipment(Equipment delEquip) throws SQLException
	{
		equipDao.delete(delEquip);
	}
	
	public void updateEquipment(Equipment updEquip) throws SQLException
	{
		equipDao.update(updEquip);
	}
	
	public void createOrUpdateEquipment(Equipment equip) throws SQLException
	{
		equipDao.createOrUpdate(equip);
	}
	
	public List<Equipment> selectAllEquipments() throws SQLException
	{
		return equipDao.queryForAll();
	}
}
