package proj.thw.app.activitys;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import proj.thw.app.R;
import proj.thw.app.adapters.ThwTreeViewAdapter;
import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.treeview.InMemoryTreeStateManager;
import proj.thw.app.treeview.TreeBuilder;
import proj.thw.app.treeview.TreeStateManager;
import proj.thw.app.treeview.TreeViewList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Demo activity showing how the tree view can be used.
 * 
 */
public class EquipmentTreeViewListActivity extends Activity {

	public static final int KEY_REQUEST_IMPORT = 4711;
	protected static final String LOG = "EquipmentTreeViewListActivity";
	protected static final String KEY_EQUIPMENTLIST = "equip.list";
	protected static final String KEY_EQUIPMENT = "equip";

	private final Set<Equipment> selected = new HashSet<Equipment>();
	private TreeViewList tvlEquipment;

	//private static final int LEVEL_NUMBER = 6;
	private TreeStateManager<Equipment> manager = new InMemoryTreeStateManager<Equipment>();
	private TreeBuilder<Equipment> treeBuilder;
	
	private ThwTreeViewAdapter simpleAdapter;
	private boolean collapsible;

	private ArrayList<Equipment> equipmentList;
	private OrmDBHelper dbHelper;
	private ProgressDialog loadDialog;
	
	private TextView txttest;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_tree_view_list);
		
		tvlEquipment = (TreeViewList) findViewById(R.id.tvlequip);
		dbHelper = new OrmDBHelper(this);

		init();
		
	}

	
	
	private void init()
	{
		txttest = (TextView) findViewById(R.id.txttest);
		loadDialog = new ProgressDialog(this);
		loadDialog.setTitle("Please Wait...");
		loadDialog.setMessage("Load Data from DB!");
		loadDialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {

					equipmentList = (ArrayList<Equipment>)dbHelper.getDbHelperEquip().selectAllEquipments();
				} catch (SQLException e) {
					Log.e(LOG, e.getMessage());
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						manager = new InMemoryTreeStateManager<Equipment>(); 
						treeBuilder = new TreeBuilder<Equipment>(manager);
						loadDialog.dismiss();
						int maxLayer  = 1;
						for(Equipment loadedItem : equipmentList)
						{
							treeBuilder.sequentiallyAddNextNode(loadedItem, loadedItem.getLayer() -1);
							if(maxLayer < loadedItem.getLayer())
								maxLayer = loadedItem.getLayer();
						}
						
						simpleAdapter = new ThwTreeViewAdapter(EquipmentTreeViewListActivity.this,
																					selected,
																					manager,
																					maxLayer);
						tvlEquipment.setAdapter(simpleAdapter);
						tvlEquipment.setCollapsible(true);
						txttest.setText("Anzahl Nodes: "+equipmentList.size());
					}
				});
			}
		}).start();
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.importdata:
			intent= new Intent(this, ImportDataActivity.class);
			startActivityForResult(intent, KEY_REQUEST_IMPORT);
			break;
		case R.id.exportdata:
			intent = new Intent(this, ExportDataActivity.class);
			startActivity(intent);
			break;
		default: // Do Nothing...
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case KEY_REQUEST_IMPORT: 
				if(resultCode == RESULT_OK)
					init();
			break;
		default: //Do Nothing..
		}
	}

/*
	 @Override
	 public boolean onContextItemSelected(final MenuItem item) {
		 final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo(); final long id = info.id; if (item.getItemId() ==
	  R.id.context_menu_collapse) { manager.collapseChildren(id); return true;
	  } else if (item.getItemId() == R.id.context_menu_expand_all) {
	  manager.expandEverythingBelow(id); return true; } else if
	  (item.getItemId() == R.id.context_menu_expand_item) {
	  manager.expandDirectChildren(id); return true; } else if
	  (item.getItemId() == R.id.context_menu_delete) {
	  manager.removeNodeRecursively(id); return true; } else { return
	  super.onContextItemSelected(item); } }
*/

	public void onClickTestButton(View view) {
		Intent in = new Intent(this, DetailListActivity.class);

		ArrayList<Equipment> test = new ArrayList<Equipment>();
		for (int i = 5; i < 11; i++) {
			test.add(equipmentList.get(i));
		}

		in.putExtra(KEY_EQUIPMENTLIST, test);
		startActivity(in);
	}
	
	
}
