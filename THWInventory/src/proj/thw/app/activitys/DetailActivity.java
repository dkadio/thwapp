//TODO status wird beim ersten mal beim laden geloescht weil man nicht anders an die checkboxen kommt ausser ueber die onclickmethode --> saubere loesung waere nett 
package proj.thw.app.activitys;

import java.util.ArrayList;
import java.util.Vector;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.Equipment.Status;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {

	static final String MYTAG = "DetailActivity.class";

	EditText etequipNo, etdeviveNo, etinvNo, etStatus;
	TextView tvtype, tvdescription, tvSoll, tvIst, tvdebug;
	CheckBox cbforeignpart;
	ImageView imageequip;

	Vector<Equipment.Status> selectedstates;
	ArrayList<Equipment.Status> allstates;
	ArrayList<Equipment> equipments;
	int selectedItem;
	ListPopupWindow popuplist;
	Activity thisact = this;
	boolean firsttime;

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
		popuplist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {

				CheckedTextView ct = (CheckedTextView) v;
				if (firsttime) {
					selectedstates.clear();
					firsttime = false;
				}
				if (!ct.isChecked()) {
					// schreib das teil rein#
					selectedstates.add((Status) popuplist.getListView()
							.getItemAtPosition(pos));
					ct.setChecked(true);
					Log.d(MYTAG, selectedstates.toString());
					etStatus.setText(selectedstates.toString());
				} else {
					// hol den status raus
					selectedstates.remove(popuplist.getListView()
							.getItemAtPosition(pos));
					ct.setChecked(false);
					etStatus.setText(selectedstates.toString());
					Log.d(MYTAG, selectedstates.toString());
				}
			}

		});

	}

	private void setValues() {
		tvdebug.setText(String.valueOf(selectedItem) + "/" + equipments.size());
		
		imageequip.setImageBitmap(equipments.get(selectedItem).getEquipImg().getImg());

		
		etdeviveNo.setText(equipments.get(selectedItem).getDeviceNo());
		etequipNo.setText(equipments.get(selectedItem).getEquipNo());
		etinvNo.setText(equipments.get(selectedItem).getInvNo());

		cbforeignpart.setChecked(equipments.get(selectedItem).isForeignPart());

		tvdescription.setText(equipments.get(selectedItem).getDescription());
		tvtype.setText(equipments.get(selectedItem).getType().toString());

		tvIst.setText(String.valueOf(equipments.get(selectedItem)
				.getActualQuantity()));
		tvSoll.setText(String.valueOf(equipments.get(selectedItem)
				.getTargetQuantity()));

		etStatus.setText(equipments.get(selectedItem).getStatus().toString());
		selectedstates = equipments.get(selectedItem).getStatus();
		firsttime = true;
		setPopUpList();

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
		tvdebug = (TextView) findViewById(R.id.debug);
		
		imageequip = (ImageView) findViewById(R.id.imageEquip);
		
		etdeviveNo = (EditText) findViewById(R.id.editTextDeviceNo);
		etequipNo = (EditText) findViewById(R.id.editTextEquipNo);
		etinvNo = (EditText) findViewById(R.id.editTextInvNo);
		etStatus = (EditText) findViewById(R.id.editTextStatus);

		tvdescription = (TextView) findViewById(R.id.textViewProducer);
		tvtype = (TextView) findViewById(R.id.textViewTyp);

		tvIst = (TextView) findViewById(R.id.TextViewIst);
		tvSoll = (TextView) findViewById(R.id.textViewSoll);

		cbforeignpart = (CheckBox) findViewById(R.id.checkBoxforeignPart);

		// disable some views
		etequipNo.setEnabled(false);
		cbforeignpart.setEnabled(false);
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
		case R.id.takePicture:
			equipments.get(selectedItem).getEquipImg().getImg();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void nextItem(View v) {
		if (selectedItem < equipments.size() - 1) {
			saveValues();
			selectedItem++;
			setValues();
		}
	}

	public void previousItem(View v) {
		if (selectedItem > 0) {
			saveValues();
			selectedItem--;
			setValues();

		}
	}

	private void saveValues() {
		// equipments.get(selectedItem).setEquipNo(etequipNo.getText().toString());
		equipments.get(selectedItem).setDeviceNo(
				etdeviveNo.getText().toString());
		equipments.get(selectedItem).setInvNo(etinvNo.getText().toString());
		equipments.get(selectedItem).setForeignPart(cbforeignpart.isChecked());
		// status speichern und vector zuruecksetzen
		equipments.get(selectedItem).setStatus(selectedstates);
		equipments.get(selectedItem).setActualQuantity(
				Integer.valueOf(tvIst.getText().toString()));
		
	}

	public void plusCount(View v) {
		// TODO darf das groeser werden als Soll?
		// if(Integer.valueOf(tvIst.getText().toString()) <
		// Integer.valueOf(tvSoll.getText().toString()))
		tvIst.setText(String.valueOf(Integer
				.valueOf(tvIst.getText().toString()) + 1));

	}

	public void minusCount(View v) {
		if (Integer.valueOf(tvIst.getText().toString()) > 0)
			tvIst.setText(String.valueOf(Integer.valueOf(tvIst.getText()
					.toString()) - 1));
	}

	public void stop(View v) {
	}

}
