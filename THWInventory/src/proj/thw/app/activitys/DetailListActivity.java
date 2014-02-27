//TODO anderen Adapter setzen der bilder mit anzeigt
//TODO nach verschiedenen kritereien im baum suchen und diese dann in der detail activity anzeigen
package proj.thw.app.activitys;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.tools.Helper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * geplant als Uebersicht der gewaehlten Equipments und als Pruefung dieser
 * @author deniz
 *
 */
public class DetailListActivity extends Activity implements OnItemClickListener {
	public final static String KEY_EQUIP_COLLECTION = "equip_collection_key";
	public final static String KEY_SELECTED_EQUIP = "selected_equip_key";
	public static final String KEY_FOR_TREEVIEW_RESULT = "the.equipment.returns.to.the.treeview";
	public static final String KEY_SAVE_THE_EQUIPMENT = "save.the.equipment.for.death";
	public final static int REQUEST_CODE = 98613123;
	private final static String MYTAG = "DetailListActivity";
	ArrayList<Equipment> equipments;
	ListView equipmentListView;
	Intent intent;
	OrmDBHelper dbHelper;
	Context context;
	ProgressDialog loadDialog;
	boolean isupdated = false;
	ArrayList<Equipment> oldlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(MYTAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (getIntent().hasExtra(
				EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST)) {
			try {
				String pathTotempFile = getIntent().getExtras().getString(EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST);
				equipments = (ArrayList<Equipment>) Helper.FileStreamToList(pathTotempFile);
				context = this;

				dbHelper = new OrmDBHelper(this);

				intent = new Intent(this, DetailActivity.class);

				initView();
				TextView many = (TextView) findViewById(R.id.eintraege);
				many.setText(getResources().getString(R.string.listsize)+ ": "
						+ String.valueOf(equipments.size()));
				Log.d(MYTAG, "onCreate() --- ende");
			} catch (ClassNotFoundException e) {
				Log.e(this.getClass().getName(), e.getMessage());
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			/*equipments = (ArrayList<Equipment>) getIntent().getExtras()
					.getSerializable(
							EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST);*/
			oldlist = equipments;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			resultintent();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Initialisiert die versch. Views
	 */
	private void initView() {
		Log.d(MYTAG, "initviews()");
		equipmentListView = (ListView) findViewById(R.id.equipmentListView);
		equipmentListView.setAdapter(new ArrayAdapter<Equipment>(this,
				android.R.layout.simple_list_item_1, equipments));
		equipmentListView.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		// TODO start detailactivity fuer das gewuwnschte equipment
		
		String tempFolderPath = Environment.getExternalStorageDirectory() 
				+ File.separator 
				+ getResources().getString(R.string.app_name) 
				+ File.separator 
				+ SplashScreenActivity.FOLDER_TEMP;
		
		File tempFile;
		try {
			tempFile = Helper.ListToFileStream(equipments,tempFolderPath);
			intent.putExtra(KEY_EQUIP_COLLECTION, tempFile.getAbsolutePath());
			intent.putExtra(KEY_SELECTED_EQUIP, position);
			startActivityForResult(intent, REQUEST_CODE);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(MYTAG, "onActivityResult()");
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			Log.d(MYTAG, "onActivityResult() - result ok");

			if(data.getExtras().containsKey(DetailActivity.KEY_RESULT_INTENT_EQUIPMENT))
			{
				String pathTempFile = data.getExtras().getString(DetailActivity.KEY_RESULT_INTENT_EQUIPMENT);
				try {
					equipments = (ArrayList<Equipment>)  Helper.FileStreamToList(pathTempFile);
					Log.d(MYTAG, "updated equipmentlist");
				} catch (ClassNotFoundException e) {
					Log.e(this.getClass().getName(), e.getMessage());
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Log.e(this.getClass().getName(), e.getMessage());
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
			/*equipments = (ArrayList<Equipment>) data
					.getSerializableExtra(DetailActivity.KEY_RESULT_INTENT_EQUIPMENT);*/
//			for(Equipment a : equipments){
//				if(a.getEquipImg().getImg() == null){
//					Log.d(MYTAG, "0");
//				}else{
//					Log.d(MYTAG, "1");
//				}
//					
//			}
			isupdated = true;
			Log.d(MYTAG, "start save to db");
			saveToDB();

		} else {
			isupdated = false;
			Toast.makeText(
					this,
					"Da is wohl was beim ueberreichen der Dataen was schief gelaufen! musche nommo zaehlen",
					Toast.LENGTH_SHORT); // TODO Was sollte man in diesem fall
											// tun? is ja eh alles zu spaet
		}
		Log.d(MYTAG, "onActivityResult() --- Ende");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(MYTAG, "onSaveInstanceState()");

		outState.putSerializable(KEY_SAVE_THE_EQUIPMENT, equipments);
		Log.d(MYTAG, "onSaveInstanceState() --- Ende");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(MYTAG, "onRestoreInstanceState()");

		super.onRestoreInstanceState(savedInstanceState);
		equipments = (ArrayList<Equipment>) savedInstanceState
				.getSerializable(KEY_SAVE_THE_EQUIPMENT);
		initView();
		Log.d(MYTAG, "onRestoreInstanceState()--Ende");

	}

	@Override
	protected void onPause() {
		super.onPause();
		resultintent();
	}

	/**
	 * Gibt das ergebnis an die Treeview zurueck
	 */
	private void resultintent() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(KEY_FOR_TREEVIEW_RESULT, isupdated);
		Log.d(MYTAG, String.valueOf(isupdated));
		setResult(RESULT_OK, resultIntent);
	}

	/**
	 * Speichert die geaenderten Daten wenn der User das moechte
	 */
	public void saveToDB() {
		loadDialog = new ProgressDialog(this);

		new AlertDialog.Builder(this)
				.setTitle("Achtung")
				.setMessage(
						"Moechten Sie die Aenderungen in der DB beibehalten?")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d(MYTAG, "ja clicked");

						loadDialog.setTitle("Please Wait...");
						loadDialog.setMessage("Saving Data to DB...");
						loadDialog.setIcon(getResources().getDrawable(R.drawable.db_icon));
						loadDialog.setCanceledOnTouchOutside(false);
						loadDialog.show();
						Log.d(MYTAG, "start thread");
						new Thread(new Runnable() {

							@Override
							public void run() {
								Log.d(MYTAG, "speicher objekte");
								for (Equipment saveItem : equipments) {
									try {
//										if(saveItem.getEquipImg().getImg() == null){
//											Log.d(MYTAG, "0");	
//										}else{
//											Log.d(MYTAG, "1");
//										}
										
										dbHelper.getDbHelperEquip().updateEquipment(saveItem);
									} catch (SQLException e) {
										Log.e(EquipmentTreeViewListActivity.class.getName(),
												e.getMessage());
										Toast.makeText(context,
												"Fehler beim speichern: " + e.getMessage(),
												Toast.LENGTH_LONG).show();
									}
								}

								loadDialog.dismiss();

							}
						}).start();
					}
				})
				.setNegativeButton("Nein",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.d(MYTAG, "nein clicked");

								equipments = oldlist;
								isupdated = false;			
								initView();

							}
						}).show();
		
		

	}
}
