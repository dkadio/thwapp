package proj.thw.app.activitys;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
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
import android.widget.Spinner;
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
	private Spinner spState;

	private static Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;

		setContentView(R.layout.activity_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getIntentContent();
		initViews();
		setValues(firstSelectedEquip);
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
		
		setSpinnerValues(e);

	}

	private void setSpinnerValues(Equipment e) {
		
	}

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
		cbForeignPart.setClickable(false); //soll nur auf true stehen wenn chris das aenderbar machen will
		spState = (Spinner) findViewById(R.id.spinnerState);
		//TODO Spinner Adapter mit allen States von Equip setzen

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
			//TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem benutzer das klar
			saveCurrentEquip();
			currentSelectedEquip -= 1;
			setValues(currentSelectedEquip);
			break;
		case R.id.imageButtonForward:
			// addiere ein zu currentSelected und zeig die werte an
			//TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem benutzer das klar
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
			//TODO pruefe ob alle werte vorhanden sind wenn nicht mach dem benutzer das klar
			saveCurrentEquip();
			//TODO return intent mit arraylist
			
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

	private void saveCurrentEquip() {
		Equipment e = equipments.get(currentSelectedEquip);
		e.setActualQuantity(Integer.valueOf(tvIst.getText().toString()));
		e.setInvNo(String.valueOf(etInvNo.getText()));
		e.setDeviceNo(String.valueOf(etDeviceNo.getText()));
		
	}

	private void plusOne() {
		equipments.get(currentSelectedEquip).setActualQuantity(
				equipments.get(currentSelectedEquip).getActualQuantity() + 1);
	}
	
	private void minusOne(){
		equipments.get(currentSelectedEquip).setActualQuantity(
				equipments.get(currentSelectedEquip).getActualQuantity() - 1);
	}

}
