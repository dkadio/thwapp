package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import proj.thw.app.classes.Equipment;
import adapters.EquipmentListAdapter;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DetailListActivity extends Activity implements OnItemClickListener{
	public final static String EQUIP_COLLECTION_KEY = "equip_collection_key";
	public final static String SELECTED_EQUIP_KEY = "selected_equip_key";
	ArrayList<Equipment> equipments;
	ListView equipmentListView;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		equipments = (ArrayList<Equipment>) getIntent()
				.getExtras()
				.getSerializable(
						"hier kommt der key rein mit dem max mir die arraylist gibt");
		intent = new Intent(this, DetailActivity.class);
		intent.putExtra(EQUIP_COLLECTION_KEY, equipments);
		initView();
		registerForContextMenu(equipmentListView);

	}
	


	private void initView() {

		equipmentListView = (ListView) findViewById(R.id.equipmentListView);
		EquipmentListAdapter ea = new EquipmentListAdapter(this, equipments);
		equipmentListView.setAdapter(ea);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 123:

			// TODO do some shit
			return true;

		default:
			return super.onContextItemSelected(item);

		}

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
		intent.putExtra(SELECTED_EQUIP_KEY, position);
		startActivity(intent);
	}

}