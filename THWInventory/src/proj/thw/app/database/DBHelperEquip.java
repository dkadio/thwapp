package proj.thw.app.database;

import java.sql.SQLException;
import java.util.List;

import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.EquipmentImage;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

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
	
	public List<Equipment> selectEquipmentList(String whereClause, Object value) throws SQLException
	{
		QueryBuilder<Equipment,Integer> queryBuilder = equipDao.queryBuilder();
		queryBuilder.where().eq(whereClause,value);
		return queryBuilder.query();
	}
	
	public Equipment selectEquipment(String whereClause, Object value) throws SQLException
	{
		QueryBuilder<Equipment,Integer> queryBuilder = equipDao.queryBuilder();
		queryBuilder.where().eq(whereClause,value);
		List<Equipment> rslt = queryBuilder.query();
		
		if(rslt.size() > 0)
			return rslt.get(0);
		
		return null;
	}
	
	
	
	/*
	public List<Equipment> selectEquipment(String whereClause, int value) throws SQLException
	{
		QueryBuilder<Equipment,Integer> queryBuilder = equipDao.queryBuilder();
		queryBuilder.where().eq(whereClause,value);
		return queryBuilder.query();
	}*/
	
	public void refresh(Equipment refreshEquip) throws SQLException
	{
		equipDao.refresh(refreshEquip);
	}
	
	
}
