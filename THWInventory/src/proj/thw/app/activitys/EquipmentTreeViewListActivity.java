package proj.thw.app.activitys;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import proj.thw.app.R;
import proj.thw.app.adapters.ThwTreeViewAdapter;
import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.tools.Helper;
import proj.thw.app.treeview.InMemoryTreeStateManager;
import proj.thw.app.treeview.OnTreeViewListItemClickListener;
import proj.thw.app.treeview.TreeBuilder;
import proj.thw.app.treeview.TreeStateManager;
import proj.thw.app.treeview.TreeViewList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class EquipmentTreeViewListActivity extends Activity {

	public static final int KEY_REQUEST_IMPORT = 4711;
	public static final int KEY_REQUEST_EXPORT = 4712;
	public static final int KEY_REQUEST_DETAILLIST = 4713;
	protected static final String LOG = "EquipmentTreeViewListActivity";
	protected static final String KEY_EQUIPMENTLIST = "equip.list";
	protected static final String KEY_EQUIPMENT = "equip";

	private TreeViewList tvlEquipment;

	// private static final int LEVEL_NUMBER = 6;
	private TreeStateManager<Equipment> manager = new InMemoryTreeStateManager<Equipment>();
	private TreeBuilder<Equipment> treeBuilder;

	private ThwTreeViewAdapter simpleAdapter;
	private ArrayList<Equipment> equipmentList;
	private OrmDBHelper dbHelper;
	private ProgressDialog loadDialog;
	private ProgressBar pbLoadTreeView;

	private TextView tvTreeSize;
	private SearchView searchView;
	private Context context;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		setContentView(R.layout.activity_tree_view_list);
		context = this;

		tvTreeSize = (TextView) findViewById(R.id.tvtreesize);
		pbLoadTreeView = (ProgressBar) findViewById(R.id.pbloadtreeview);
		tvlEquipment = (TreeViewList) findViewById(R.id.tvlequip);
		tvlEquipment.setTreeViewListItemClickListener(new OnTreeViewListItemClickListener() {

					@Override
					public void onTreeViewListItemClick(AdapterView<?> parent,
							View view, int position, long id) {

						// getClickedItem
						final Equipment equipitem = (Equipment) view.getTag();
						manager.expandEverythingBelow(equipitem);
						pbLoadTreeView.setVisibility(View.VISIBLE);
						new Thread(new Runnable() {

							@Override
							public void run() {

								ArrayList<Equipment> selectedList = new ArrayList<Equipment>(
										manager.getAbsoluteChildren(equipitem));
									
								callDetailListActivity(selectedList);
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										pbLoadTreeView
												.setVisibility(View.INVISIBLE);
									}
								});
							}
						}).start();
					}
				});
		dbHelper = new OrmDBHelper(this);
		init();
	}
	
	/**
	 * Funktion, um DateiListActivity aufzurufen
	 * @param callList Liste mit den Equipments die uebergeben werden soll
	 */
	private void callDetailListActivity(List<Equipment> callList)
	{
		//uebergabe ueber eine File, da uebergabe auf 1 MB
		//beschraenkt ist und keine Exception zum abfangen
		//geworfen wird
		String tempFolderPath = Environment.getExternalStorageDirectory() 
								+ File.separator 
								+ getResources().getString(R.string.app_name) 
								+ File.separator 
								+ SplashScreenActivity.FOLDER_TEMP;
		
		try {
			File tempFile = Helper.ListToFileStream(callList,tempFolderPath);
			Intent in = new Intent(context,
					DetailListActivity.class);
			in.putExtra(KEY_EQUIPMENTLIST, tempFile.getAbsolutePath());
			startActivityForResult(in,
					KEY_REQUEST_DETAILLIST);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();	
		}
		
	}

	/**
	 * Funktion die den Tree initialisiert und anzeigt
	 */
	private void init() {
		loadDialog = new ProgressDialog(this);
		loadDialog.setTitle(getResources().getString(R.string.please_wait));
		loadDialog.setMessage(getResources().getString(R.string.load_from_db));
		loadDialog.setIcon(getResources().getDrawable(R.drawable.db_icon));
		loadDialog.setCanceledOnTouchOutside(false);
		loadDialog.show();

		//Thread zum laden der Daten aus der DB und zum erstellen des Tree's
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					// Alle Equipments aus der DB laden, um dem Tree hinzuzufuegen
					equipmentList = (ArrayList<Equipment>) dbHelper
							.getDbHelperEquip().selectAllEquipments();
				} catch (SQLException e) {
					Log.e(LOG, e.getMessage());
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						manager = new InMemoryTreeStateManager<Equipment>();
						treeBuilder = new TreeBuilder<Equipment>(manager);
						loadDialog.dismiss();
						int maxLayer = 1;
						for (Equipment loadedItem : equipmentList) {
							if (!loadedItem
									.getType()
									.toString()
									.toUpperCase()
									.equals(Equipment.Type.NOTYPE.toString()
											.toUpperCase())) {
								treeBuilder.sequentiallyAddNextNode(loadedItem,
										loadedItem.getLayer() - 1);
								if (maxLayer < loadedItem.getLayer())
									maxLayer = loadedItem.getLayer();
							}
						}

						simpleAdapter = new ThwTreeViewAdapter(
								EquipmentTreeViewListActivity.this, null,
								manager, maxLayer);
						tvlEquipment.setAdapter(simpleAdapter);
						tvlEquipment.setCollapsible(true);
						manager.expandEverythingBelow(null);
						tvTreeSize.setText(getResources().getString(
								R.string.treesize)
								+ ": " + equipmentList.size());
					}
				});
			}
		}).start();
	}

	/**
	 * Funktion, die das Menue erstellt
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				List<Equipment> searchList = searchItems(query);

				if (searchList.isEmpty()) {
					Toast.makeText(context, "Kein Eintrag gefunden!",
							Toast.LENGTH_SHORT).show();
				} else {
					searchView.clearFocus();
					manager.expandEverythingBelow(null);
					getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					tvlEquipment.setSelection(equipmentList.indexOf(searchList
							.get(0)) - 1);
					// tvlEquipment.getChildAt(equipmentList.indexOf(searchList.get(0))).setBackgroundColor(Color.GREEN);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	/**
	 * Suchfunktion, um ein Item im Tree zu suchen
	 * @param query Equip-No 
	 * @return eine Liste mit gefundenen eintraegen
	 */
	private List<Equipment> searchItems(String query) {
		ArrayList<Equipment> searchList = new ArrayList<Equipment>();
		// gibt hier eine Liste mit allen gefunden items zurueck
		// derzeit wird aber nur das erste beruecksichtigt
		// kann erweitert werden...
		for (Equipment searchItem : equipmentList) {
			if (searchItem.getEquipNo().equals(query)) {
				searchList.add(searchItem);
			}
		}
		return searchList;
	}

	/**
	 * Auswertung, welcher Button im Menue gedrueckt wurde
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.refresh:
			init();
			break;
		case R.id.importdata:
			intent = new Intent(this, ImportDataActivity.class);
			startActivityForResult(intent, KEY_REQUEST_IMPORT);
			break;
		case R.id.exportdata:
			intent = new Intent(this, ExportDataActivity.class);
			startActivity(intent);
			break;
		case R.id.collapsed:
			if (item.isChecked()) {
				item.setChecked(false);
				manager.expandEverythingBelow(null);
			} else {
				item.setChecked(true);
				manager.collapseChildren(null);
			}

			break;
		default: // Do Nothing...
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Funktion, die die Rueckgaben der aufgerufenen Activitys auswertet
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case KEY_REQUEST_IMPORT:
			if (resultCode == RESULT_OK)
				init();
			break;
		case KEY_REQUEST_DETAILLIST:
			if (resultCode == RESULT_OK) {
				if(data.getExtras().containsKey(DetailListActivity.KEY_FOR_TREEVIEW_RESULT))
				{
					boolean t = data.getExtras().getBoolean(DetailListActivity.KEY_FOR_TREEVIEW_RESULT);
					if(t)
					{
						init();
					}
				}
			}

			break;
		default: // Do Nothing...
		}
	}
	
	/**
	 * Funktion, die aufgerufen wird, wenn der "Go" button gedrueckt wird
	 * @param view
	 */
	public void onClickGoButton(View view) {
		ArrayList<Equipment> checkedList = new ArrayList<Equipment>(
				simpleAdapter.getSelected());
		if (checkedList.isEmpty()) {
			Toast.makeText(this, getResources().getString(R.string.error_no_item_checked),
					Toast.LENGTH_LONG).show();
		} else {
			callDetailListActivity(checkedList);
		}

	}

	/**
	 * Funktion, die aenderungen an der Tree in die DB uebernimmt
	 * wurde in die DetailListActivity uebernommen
	 */
	@Deprecated
	public void saveToDB() {
		loadDialog = new ProgressDialog(this);
		loadDialog.setTitle(getResources().getString(R.string.please_wait));
		loadDialog.setMessage(getResources().getString(R.string.save_to_db));
		loadDialog.setCanceledOnTouchOutside(false);
		loadDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (Equipment saveItem : equipmentList) {
					try {
						dbHelper.getDbHelperEquip().updateEquipment(saveItem);
					} catch (SQLException e) {
						Log.e(EquipmentTreeViewListActivity.class.getName(),
								e.getMessage());
						Toast.makeText(context,
								getResources().getString(R.string.error_save) + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}

				loadDialog.dismiss();
			}
		}).start();
	}

	
	/**
	 * Funktion, die vor dem Beenden der App eine Abfrage stellt, ob Beendet werden soll
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK)
			return super.onKeyDown(keyCode, event);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.sure))
				.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();

		return super.onKeyDown(keyCode, event);
	}

}
