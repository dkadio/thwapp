package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import proj.thw.app.classes.Equipment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DetailActivity extends Activity {
	private ArrayList<Equipment> equipments;
private int selectedEquip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle bundle = getIntent().getExtras();
		equipments = (ArrayList<Equipment>) bundle.getSerializable(DetailListActivity.EQUIP_COLLECTION_KEY);
		selectedEquip = bundle.getInt(DetailListActivity.SELECTED_EQUIP_KEY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
