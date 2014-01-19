//TODO anderen Adapter setzen der bilder mit anzeigt
//TODO schiest die app wenn sie aus der Detaillist ueber homebutton gestartet wird
package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DetailListActivity extends Activity implements OnItemClickListener {
	public final static String KEY_EQUIP_COLLECTION = "equip_collection_key";
	public final static String KEY_SELECTED_EQUIP = "selected_equip_key";
	public final static int REQUEST_CODE = 98613123;
	ArrayList<Equipment> equipments;
	ListView equipmentListView;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(getIntent().hasExtra(EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST)){
		equipments = (ArrayList<Equipment>) getIntent().getExtras()
				.getSerializable(
						EquipmentTreeViewListActivity.KEY_EQUIPMENTLIST);
		}
		intent = new Intent(this, DetailActivity.class);
		intent.putExtra(KEY_EQUIP_COLLECTION, equipments);
		initView();
		
		
	}

	private void initView() {

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
		intent.putExtra(KEY_SELECTED_EQUIP, position);
		startActivityForResult(intent, REQUEST_CODE);
	}

}
