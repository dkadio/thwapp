package proj.thw.app.activitys;

import java.util.ArrayList;
import java.util.Vector;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.Equipment.Status;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;

public class DetailActivity extends Activity {
	private ArrayList<Equipment> equipments;
	private int firstSelectedEquip;
	private int currentSelectedEquip;
	private TextView tvDeviceNo, tvEquipNr, tvInvNo, tvIst, tvSoll, tvState,
			tvType;
	private ImageButton ibForward, ibBackward, ibStop, ibPosCounter;
	private EditText etDeviceNo, etInvNo;
	private CheckBox cbForeignPart;
	private ListPopupWindow listpopupwindow;
	private static Context c;
	Vector<Equipment.Status> completestatuslist;
	Vector<Equipment.Status> equipmentStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;

		setContentView(R.layout.activity_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getIntentContent();
		initStatusVector();

		initViews();
		setValues(firstSelectedEquip);

	}

	/**
	 * initialisiert einen Vektor mit allen Verfuegbaren Werten vor und
	 * initialisiert einen zweiten mit den werten von dem equipment
	 */
	private void initStatusVector() {
		// TODO Auto-generated method stub
		completestatuslist = new Vector<Equipment.Status>();
		for (int i = 0; i < Equipment.Status.values().length; i++) {
			completestatuslist.add(Equipment.Status.values()[i]);
		}
		equipmentStatus = equipments.get(currentSelectedEquip).getStatus();

	}

	private void setValues(int selectedEquip) {
		Equipment e = equipments.get(selectedEquip);

		tvDeviceNo.setText("DeviceNo:");
		etDeviceNo.setText(e.getDeviceNo());

		tvInvNo.setText("InvNo: ");
		etInvNo.setText(e.getInvNo());

		tvEquipNr.setText("EquipNr: " + e.getEquipNo());
		tvIst.setText(e.getActualQuantity());
		tvSoll.setText(e.getTargetQuantity());

		cbForeignPart.setChecked(e.isForeignPart());

		tvType.setText(e.getType().toString());

		setListvalues();
	}

	/**
	 * laeuft beide status vekoten ab und vergleicht welche bereits in dem equip
	 * vorhanden sind setzt entsprechend die checkboxen auf true/false
	 */
	private void setListvalues() {
		// vergleichn wie der adapter gesetzt werden muss
		ArrayAdapter<Equipment.Status> sa = new ArrayAdapter<Equipment.Status>(
				this, android.R.layout.simple_list_item_multiple_choice,
				completestatuslist);
		listpopupwindow.getListView().setAdapter(sa);
		for (int i = 0; i < equipmentStatus.size(); i++) {
			for (int j = 0; j < completestatuslist.size(); j++) {
				if (equipmentStatus.get(i).equals(completestatuslist.get(j))) {
					listpopupwindow.getListView().setItemChecked(i, true);
				} else {
					listpopupwindow.getListView().setItemChecked(i, false);
				}
			}
		}

	}

	/**
	 * popt die status listview auf
	 * 
	 * @param v
	 */
	public void popupTheList(View v) {
		listpopupwindow.show();
	}

	/**
	 * holt sich die mit dem Intent losgeschickten werte ab
	 */
	private void getIntentContent() {
		Bundle bundle = getIntent().getExtras();
		equipments = (ArrayList<Equipment>) bundle
				.getSerializable(DetailListActivity.EQUIP_COLLECTION_KEY);
		firstSelectedEquip = bundle
				.getInt(DetailListActivity.SELECTED_EQUIP_KEY);
		currentSelectedEquip = firstSelectedEquip; // Falls man das zuerst
													// gewaehlte noch mal
													// irgendwann brauchen solte
	}

	/**
	 * initalisiert die Views der Activity
	 */
	private void initViews() {
		tvDeviceNo = (TextView) findViewById(R.id.textViewDeviceNo);
		tvEquipNr = (TextView) findViewById(R.id.textViewEquipNr);
		tvInvNo = (TextView) findViewById(R.id.textViewInvNo);
		tvIst = (TextView) findViewById(R.id.textViewIst);
		tvSoll = (TextView) findViewById(R.id.textViewSoll);
		tvState = (TextView) findViewById(R.id.textViewState);
		tvType = (TextView) findViewById(R.id.textViewType);

		ibBackward = (ImageButton) findViewById(R.id.imageButtonBack);
		ibStop = (ImageButton) findViewById(R.id.imageButtonStop);
		ibForward = (ImageButton) findViewById(R.id.imageButtonForward);
		ibPosCounter = (ImageButton) findViewById(R.id.imageButtonPosCounter);

		etDeviceNo = (EditText) findViewById(R.id.editTextDeviceNo);
		etInvNo = (EditText) findViewById(R.id.editTextInvNo);
		cbForeignPart = (CheckBox) findViewById(R.id.checkBoxforeignpart);
		cbForeignPart.setClickable(false); // soll nur auf true stehen wenn
											// chris das aenderbar machen will
		listpopupwindow = new ListPopupWindow(this);

		listpopupwindow.getListView().setOnItemClickListener(
				new OnItemClickListener() {

					/**
					 * entfernt bzw fuegt zu dem Status des Equipments einen
					 * weiteren hinzu
					 */
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Equipment.Status state = (Status) arg0
								.getItemAtPosition(arg2);
						if (listpopupwindow.getListView().isItemChecked(arg2)) {
							// schreib das teil rein
							equipmentStatus.remove(state);
						} else {
							// hol den status raus
							equipmentStatus.add(state);
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	public void imageButtonAction(View v) {
		switch (v.getId()) {
		case R.id.imageButtonBack:
			// zieh eins von currentSelected ab und zeig die werte an
			// TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem
			// benutzer das klar
			saveCurrentEquip();
			currentSelectedEquip -= 1;
			setValues(currentSelectedEquip);
			break;
		case R.id.imageButtonForward:
			// addiere ein zu currentSelected und zeig die werte an
			// TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem
			// benutzer das klar
			saveCurrentEquip();
			currentSelectedEquip += 1;
			setValues(currentSelectedEquip);

			break;
		case R.id.imageButtonPosCounter:
			// erhoehe von dem aktuell geaehlten item den ist counter
			plusOne();
			saveCurrentEquip();
			setValues(currentSelectedEquip);
			break;
		case R.id.imageButtonStop:
			// Return die Arrayliste mit den bearbeiteten Items
			// TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem
			// benutzer das klar
			saveCurrentEquip();
			// TODO return intent mit arraylist

			break;
		case R.id.imageButtonNegCounter:
			minusOne();
			saveCurrentEquip();
			setValues(currentSelectedEquip);
			break;
		default:
			break;
		}
	}

	/**
	 * speichert die neu eingebenen werte in dem equip ab
	 */
	private void saveCurrentEquip() {
		Equipment e = equipments.get(currentSelectedEquip);
		e.setActualQuantity(Integer.valueOf(tvIst.getText().toString()));
		e.setInvNo(String.valueOf(etInvNo.getText()));
		e.setDeviceNo(String.valueOf(etDeviceNo.getText()));
		e.setStatus(equipmentStatus);
	}

	/**
	 * zaehlt den CurrentSeleceted zaehler hoch
	 */
	private void plusOne() {
		equipments.get(currentSelectedEquip).setActualQuantity(
				equipments.get(currentSelectedEquip).getActualQuantity() + 1);
	}

	/**
	 * zaehlt den CurrentSeleceted zaehler runter
	 */
	private void minusOne() {
		equipments.get(currentSelectedEquip).setActualQuantity(
				equipments.get(currentSelectedEquip).getActualQuantity() - 1);
	}

}
