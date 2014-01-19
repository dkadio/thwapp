//TODO status setzen 
package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import proj.thw.app.classes.Equipment;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class DetailActivity extends Activity {

	static final String MYTAG = "DetailActivity.class";

	EditText etequipNo, etdeviveNo, etinvNo, etStatus;
	TextView tvtype, tvdescription;
	CheckBox cbforeignpart;

	ArrayList<Equipment.Status> allstates;
	ArrayList<Equipment> equipments;
	int selectedItem;
	ListPopupWindow popuplist;
	Activity thisact = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		// Show the Up button in the action bar.
		setupActionBar();

		// initalisiere views
		init();
		// hole inhalt des intents ab
		getintentContent();

		setValues();

		setPopUpList();
		setListener();
	}

	private void setListener() {

		etStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard(thisact);
				popuplist.show();
			}
		});
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	private void setPopUpList() {

		popuplist = new ListPopupWindow(this);
		popuplist.setAdapter(new ArrayAdapter<Equipment.Status>(this,
				android.R.layout.simple_list_item_multiple_choice,
				Equipment.Status.values()));
		popuplist.setAnchorView(etStatus);
	}

	private void setValues() {
		etdeviveNo.setText(equipments.get(selectedItem).getDeviceNo());
		etequipNo.setText(equipments.get(selectedItem).getEquipNo());
		etinvNo.setText(equipments.get(selectedItem).getInvNo());

		cbforeignpart.setChecked(equipments.get(selectedItem).isForeignPart());

		tvdescription.setText(equipments.get(selectedItem).getDescription());
		tvtype.setText(equipments.get(selectedItem).getType().toString());

		etStatus.setText(equipments.get(selectedItem).getStatus().toString());

	}

	private void getintentContent() {
		equipments = (ArrayList<Equipment>) getIntent().getExtras()
				.getSerializable(DetailListActivity.KEY_EQUIP_COLLECTION);
		Log.d(MYTAG, "Array erhalten: " + equipments.toString());
		selectedItem = getIntent().getExtras().getInt(
				DetailListActivity.KEY_SELECTED_EQUIP);
		Log.d(MYTAG, "SelectedItem erhalten: " + selectedItem);
	}

	private void init() {
		etdeviveNo = (EditText) findViewById(R.id.editTextDeviceNo);
		etequipNo = (EditText) findViewById(R.id.editTextEquipNo);
		etinvNo = (EditText) findViewById(R.id.editTextInvNo);
		etStatus = (EditText) findViewById(R.id.editTextStatus);

		tvdescription = (TextView) findViewById(R.id.textViewProducer);
		tvtype = (TextView) findViewById(R.id.textViewTyp);

		cbforeignpart = (CheckBox) findViewById(R.id.checkBoxforeignPart);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void nextItem(View v) {
		// next implementieren
	}

	public void previousItem(View v) {
	}

	public void plusCount(View v) {
	}

	public void minusCount(View v) {
	}

	public void stop(View v) {
	}

}
