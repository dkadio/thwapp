package proj.thw.app.ie;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

import proj.thw.app.R;
import proj.thw.app.activitys.ImportDataActivity;
import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.EquipmentImage;
import proj.thw.app.database.OrmDBHelper;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.csvreader.CsvReader;

/**
 * Importroutinen als AsyncTask
 * @author max / deniz
 *
 */
public class ThwCsvImporter extends AsyncTask<FilePackage, String, Boolean> {

	private static final Charset set = Charset.forName("ISO-8859-1");
	static final String COLUMN_LAYER = "Ebene";
	static final String COLUMN_OE = "OE";
	static final String COLUMN_TYPE = "Art";
	static final String COLUMN_FB = "FB";
	static final String COLUMN_QUANTITY = "Menge";
	static final String COLUMN_STOCK = "Verfügbar";
	static final String COLUMN_ACTUALQUANTITY = "Menge Ist";
	static final String COLUMN_DESCRIPTION = "Ausstattung | Hersteller | Typ";
	static final String COLUMN_EQUIP_NO = "Sachnummer";
	static final String COLUMN_INV_NO = "Inventar Nr";
	static final String COLUMN_DEVICE_NO = "Gerätenr.";
	static final String COLUMN_STATUS = "Status";
	static final String COLUMN_IMAGE = "Image";
	static final String COLUMN_ID = "Id";
	static final String COLUMN_STREAM = "Stream";

	private ProgressDialog asyncDialog;

	private OrmDBHelper dbHelper;
	private ImportDataActivity callContext;
	private TextView tvStatus;
	CsvReader readerImage = null;
	CsvReader reader = null;

	EquipmentImage defaultEquipImg;

	public ThwCsvImporter(OrmDBHelper dbHelper, ImportDataActivity callContext,
			TextView tvStatus) {
		this.dbHelper = dbHelper;
		this.callContext = callContext;
		this.tvStatus = tvStatus;
	}

	public ThwCsvImporter(OrmDBHelper dbHelper, ImportDataActivity callContext) {
		this.dbHelper = dbHelper;
		this.callContext = callContext;
		this.tvStatus = null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (tvStatus != null) {
			tvStatus.setVisibility(View.VISIBLE);
		} else {
			asyncDialog = new ProgressDialog(callContext);
			asyncDialog.setCanceledOnTouchOutside(false);
			asyncDialog.setTitle(callContext.getString(R.string.please_wait));
			asyncDialog.setMessage(callContext.getString(R.string.importing));
			asyncDialog.setIcon(callContext.getResources().getDrawable(
					R.drawable.db_icon));
			asyncDialog.show();
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (!result) {
			Toast.makeText(callContext,callContext.getString(R.string.error_import),
					Toast.LENGTH_LONG).show();
		}
		if (tvStatus != null) {
			tvStatus.setText("");
			tvStatus.setVisibility(View.INVISIBLE);
		} else {
			asyncDialog.dismiss();
			callContext.getReturnHandler().handleMessage(new Message());
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		if (tvStatus != null) {
			tvStatus.setText(values[0].toString());
		} else {
			asyncDialog.setMessage(values[0].toString());
		}
	}

	@Override
	protected Boolean doInBackground(FilePackage... params) {

		if (params.length == 0) {
			return false;
		}

		FilePackage fileToImport = params[0];

		int rowCount = 0;

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileToImport.getDataFile()
					.getFileToParse());
			reader = new CsvReader(fis,
					((CSVFile) fileToImport.getDataFile()).getSeparator(), set);
			reader.readHeaders();

			int headLayer = reader.getIndex(COLUMN_LAYER);
			int headOE = reader.getIndex(COLUMN_OE);
			int headType = reader.getIndex(COLUMN_TYPE);
			int headFB = reader.getIndex(COLUMN_FB);
			int headQuantity = reader.getIndex(COLUMN_QUANTITY);
			int headStock = reader.getIndex(COLUMN_STOCK);
			int headActualQuantity = reader.getIndex(COLUMN_ACTUALQUANTITY);
			int headDescription = reader.getIndex(COLUMN_DESCRIPTION);
			int headEquipNo = reader.getIndex(COLUMN_EQUIP_NO);
			int headInvNo = reader.getIndex(COLUMN_INV_NO);
			int headDeviceNo = reader.getIndex(COLUMN_DEVICE_NO);
			int headColumnStatus = reader.getIndex(COLUMN_STATUS);
			int headColumnImage = reader.getIndex(COLUMN_IMAGE);
			int headId = 0;
			int headImageStream = 0;

			Boolean hasImage = false;
			if (headColumnImage > -1) {
				readerImage = new CsvReader(new FileInputStream(fileToImport
						.getImageFile().getFileToParse()),
						((CSVFile) fileToImport.getImageFile()).getSeparator(),
						set);
				readerImage.setSafetySwitch(false);
				readerImage.readHeaders();
				headId = readerImage.getIndex(COLUMN_ID);
				headImageStream = readerImage.getIndex(COLUMN_STREAM);
				hasImage = true;
			}

			boolean emptyLayer = false;
			while (reader.readRecord()) {

				publishProgress(callContext.getString(R.string.loading_row_import)+ ": " + rowCount++);
				if (reader.get(headLayer).trim().isEmpty()) {
					emptyLayer = true;
				} else {
					emptyLayer = false;
				}

				String strLayer = reader.get(headLayer).trim();
				String strOE = reader.get(headOE).trim();
				String strType = reader.get(headType).trim();
				String strFB = reader.get(headFB).trim();
				String strQuantity = reader.get(headQuantity).trim();
				String strStock = reader.get(headStock).trim();
				String strActualQuantity = reader.get(headActualQuantity)
						.trim();
				String strDescription = reader.get(headDescription).trim();
				String strEquipNo = reader.get(headEquipNo).trim();
				String strInvNo = reader.get(headInvNo).trim();
				String strDeviceNo = reader.get(headDeviceNo).trim();
				String strStatus = reader.get(headColumnStatus).trim();

				String strImageId = "";
				if (hasImage) {
					strImageId = reader.get(headColumnImage).trim();
				}

				Equipment currentData = new Equipment();
				if (emptyLayer) {
					currentData.setLayer(0);
				} else {
					currentData.setLayer(parseInt(strLayer));
				}

				currentData.setLocation(strOE);
				currentData.setType(getTypeFromString(strType));
				currentData.setForeignPart(parseBool(strFB));
				currentData.setTargetQuantity(parseInt(strActualQuantity));
				currentData.setStock(parseInt(strStock));
				currentData.setActualQuantity(parseInt(strQuantity));
				currentData.setDescription(strDescription);
				currentData.setEquipNo(strEquipNo);
				currentData.setInvNo(strInvNo);
				currentData.setDeviceNo(strDeviceNo);
				currentData.setStatus(getStatusFromString(strStatus));

				if (!strImageId.isEmpty()) {
					while (readerImage.readRecord()) {
						if (readerImage.get(headId).equals(
								String.valueOf(strImageId))) {
							String strStream = readerImage.get(headImageStream);
							EquipmentImage currentImage = new EquipmentImage();
							byte[] imgBytes = Base64.decode(strStream, 0);
							currentImage.setImgBytes(imgBytes);
							currentData.setEquipImg(currentImage);
							break;
						}
					}
				}

				dbHelper.getDbHelperEquip().insertEquipment(currentData);
			}

		} catch (Exception ex) {
			try {
				fis.close();
			} catch (IOException ex1) {
				Log.e(this.getClass().getName(), ex1.getMessage());
			}
			Log.e(this.getClass().getName(), ex.getMessage());
		}

		return true;
	}

	private String getStringValue(String rowValue) {
		return rowValue.replace('"', ' ').trim();
	}

	private Vector<Equipment.Status> getStatusFromString(String status) {

		Vector<Equipment.Status> statusResultList = new Vector<Equipment.Status>();
		status = status.trim().toUpperCase();

		String[] spStatus = status.split(",");
		for (String statusItem : spStatus) {
			statusItem = getStringValue(statusItem);
			if (Equipment.Status.V.toString().equals(statusItem)) {
				statusResultList.add(Equipment.Status.V);
			} else if (Equipment.Status.F.toString().equals(statusItem)) {
				statusResultList.add(Equipment.Status.F);
			} else if (Equipment.Status.BA.toString().equals(statusItem)) {
				statusResultList.add(Equipment.Status.BA);
			} else if (Equipment.Status.A.toString().equals(statusItem)) {
				statusResultList.add(Equipment.Status.A);
			} else if(Equipment.Status.ÜB.toString().equals(statusItem)){
				statusResultList.add(Equipment.Status.ÜB);
			}else{
				statusResultList.add(Equipment.Status.V);
			}
		}
		return statusResultList;
	}

	private Equipment.Type getTypeFromString(String type) {
		Equipment.Type result = Equipment.Type.NOTYPE;
		type = type.trim().toUpperCase();

		if (type.equals(Equipment.Type.POS.toString())) {
			result = Equipment.Type.POS;
		} else if (type.equals(Equipment.Type.SATZ.toString())) {
			result = Equipment.Type.SATZ;
		} else if (type.equals(Equipment.Type.TEIL.toString())) {
			result = Equipment.Type.TEIL;
		} else if (type.equals(Equipment.Type.GWM.toString())) {
			result = Equipment.Type.GWM;
		} else {
			result = Equipment.Type.NOTYPE;
		}
		return result;
	}

	private int parseInt(String value) {
		int result = 0;
		try {
			result = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			result = 0;
		}
		return result;
	}

	private boolean parseBool(String value) {
		boolean result = false;
		if (value.trim().equals("")) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

}
