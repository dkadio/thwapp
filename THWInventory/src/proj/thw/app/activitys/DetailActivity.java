//TODO status wird beim ersten mal beim laden geloescht weil man nicht anders an die checkboxen kommt ausser ueber die onclickmethode --> saubere loesung waere nett 
//TODO shared preference fuer die attribute schreiben
package proj.thw.app.activitys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.Equipment.Status;
import proj.thw.app.classes.EquipmentImage;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
static final String KEY_SAVE_EQUIPMENTS = "key.to.save.the.equipments";
static final String KEY_SAVE_CURRENT_INDEX = "ke.to.save.the.current.index";
	static final String MYTAG = "DetailActivity.class";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;

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
	File lastmediafile;
	EquipmentImage temp;

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
		tvdebug.setText(String.valueOf(selectedItem + 1) + "/"
				+ equipments.size());

		imageequip.setImageBitmap(equipments.get(selectedItem).getEquipImg()
				.getImg());
		temp = equipments.get(selectedItem).getEquipImg();
		
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
		equipments.get(selectedItem).setEquipImg(temp);

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				setImage();
				Log.d(MYTAG, "Bild aufgenommen ok");
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				Log.d(MYTAG, "Benutzer hat Aufnahme abgebrochen canceld");
			} else {
				// Image capture failed, advise user
				Log.d(MYTAG, "Fehler beim aufnhemen eines Bildes failed");
			}
		}
	}

	private void setImage() {
		//les ein image aus dem Ordner Temp und loesche es dannach
		
		File imagefile = lastmediafile;
		FileInputStream fis = null;
		try {
		    fis = new FileInputStream(imagefile);
		    } catch (FileNotFoundException e) {
		    e.printStackTrace();
		}

		Bitmap bm = BitmapFactory.decodeStream(fis);
		int h = 100; // height in pixels
		int w = 100; // width in pixels    
		Bitmap scaled = Bitmap.createScaledBitmap(bm, h, w, true);
		imageequip.setImageBitmap(scaled);
		temp = new EquipmentImage(scaled);
		lastmediafile.delete();
		
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
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file
																// to save the
																// image
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), getResources()
						.getString(R.string.app_name)
						+ File.separator
						+ SplashScreenActivity.FOLDER_TEMP);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}
		lastmediafile=mediaFile;
		return mediaFile;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveValues();
		outState.putSerializable(KEY_SAVE_EQUIPMENTS, equipments);
		outState.putInt(KEY_SAVE_CURRENT_INDEX, selectedItem);
	}
	
	@Override
		protected void onRestoreInstanceState(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onRestoreInstanceState(savedInstanceState);
			equipments = (ArrayList<Equipment>) savedInstanceState.getSerializable(KEY_SAVE_EQUIPMENTS);
			selectedItem = savedInstanceState.getInt(KEY_SAVE_CURRENT_INDEX);
			setValues();
		}
}
