package proj.thw.app.database;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

import proj.thw.app.classes.Equipment;

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
}
