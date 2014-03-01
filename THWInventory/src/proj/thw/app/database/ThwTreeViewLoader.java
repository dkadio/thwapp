package proj.thw.app.database;

import java.sql.SQLException;
import java.util.ArrayList;

import proj.thw.app.classes.Equipment;
import proj.thw.app.treeview.TreeBuilder;
import proj.thw.app.treeview.TreeViewList;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Klasse, die den Tree Laed...
 * @author max / deniz
 *
 */
public class ThwTreeViewLoader extends AsyncTask<OrmDBHelper, Equipment, Integer> {

	private TreeBuilder<Equipment> treeBuilder;
	private ProgressDialog loadDialog;
	private Context context;
	private TreeViewList tvlEquipment;
	
	public ThwTreeViewLoader(Context context, TreeBuilder<Equipment> treeBuilder, TreeViewList tvlEquipment)
	{
		this.treeBuilder = treeBuilder;
		this.context = context;
		this.tvlEquipment = tvlEquipment;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		loadDialog = new ProgressDialog(context);
		loadDialog.setTitle("Please Wait...");
		loadDialog.setMessage("Load Data from DB!");
		loadDialog.show();
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		//tvlEquipment.setAdapter(simpleAdapter);
		tvlEquipment.setCollapsible(true);
		
		//setResult(EquipmentTreeViewListActivity.KEY_REQUEST_IMPORT);
		//tvlEquipment.registerForContextMenu(tvlEquipment);
	}
	
	@Override
	protected void onProgressUpdate(Equipment... values) {
		super.onProgressUpdate(values);
		
		Equipment insertEquip = values[0];
		
		if(insertEquip != null)
		treeBuilder.sequentiallyAddNextNode(insertEquip, insertEquip.getLayer() -1);
	}
	
	@Override
	protected Integer doInBackground(OrmDBHelper... params) {
		
		if(params.length > 0)
		{
			OrmDBHelper dbHelper = params[0];
			
			try {
				ArrayList<Equipment> loadEquipList = (ArrayList<Equipment>)dbHelper.getDbHelperEquip().selectAllEquipments();
				
				loadDialog.dismiss();
				try {
					Thread.currentThread().sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(Equipment loadedItem : loadEquipList)
				{
					publishProgress(loadedItem);
				}
				
			} catch (SQLException e) {
				Log.e(this.getClass().getName(), e.getMessage());
				return null;
			}
		}
		
		
		return null;
	}

}
