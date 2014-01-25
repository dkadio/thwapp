//TODO anderen Adapter setzen der bilder mit anzeigt
//TODO nach verschiedenen kritereien im baum suchen und diese dann in der detail activity anzeigen
package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(MYTAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (getIntent().hasExtra(
				EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST)) {
			equipments = (ArrayList<Equipment>) getIntent().getExtras()
					.getSerializable(
							EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST);	
		}
	
		intent = new Intent(this, DetailActivity.class);

		initView();
		TextView many = (TextView) findViewById(R.id.eintraege);
		many.setText("Eingetragene elemente: "
				+ String.valueOf(equipments.size()));
		Log.d(MYTAG, "onCreate() --- ende");

	}

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
		intent.putExtra(KEY_EQUIP_COLLECTION, equipments);
		intent.putExtra(KEY_SELECTED_EQUIP, position);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(MYTAG, "onActivityResult()");
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			Log.d(MYTAG, "onActivityResult() - result ok");

			equipments = (ArrayList<Equipment>) data
					.getSerializableExtra(DetailActivity.KEY_RESULT_INTENT_EQUIPMENT);
			initView();
		} else {
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
		Intent resultIntent = new Intent();
		resultIntent.putExtra(KEY_FOR_TREEVIEW_RESULT, equipments);
		setResult(RESULT_OK, resultIntent);
	}
}
