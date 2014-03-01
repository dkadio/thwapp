package proj.thw.app.ie;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
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

	static int rowCount = 0;
	int headLayer = 0;
	int headOE = 0;
	int headType = 0;
	int headFB = 0;
	int headQuantity = 0;
	int headStock = 0;
	int headActualQuantity = 0;
	int headDescription = 0;
	int headEquipNo = 0;
	int headInvNo = 0;
	int headDeviceNo = 0;
	int headColumnStatus = 0;
	int headColumnImage = 0;
	int headId = 0;
	int headImageStream = 0;
	CsvReader reader = null;
	CsvReader readerImage = null;
	Vector<Equipment> CsvAsList;
	private ProgressDialog asyncDialog;

	private OrmDBHelper dbHelper;
	private ImportDataActivity callContext;
	private TextView tvStatus;
	private HashMap<String, Integer> columnHeaders;

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
			asyncDialog.setTitle("Please Wait...");
			asyncDialog.setMessage("importiere Daten...");
			asyncDialog.setIcon(callContext.getResources().getDrawable(
					R.drawable.db_icon));
			asyncDialog.show();
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (!result) {
			Toast.makeText(callContext, "Import fehlgeschlagen...",
					Toast.LENGTH_LONG).show();
		}
		if (tvStatus != null) {
			tvStatus.setText("");
			tvStatus.setVisibility(View.INVISIBLE);
		} else {
			asyncDialog.dismiss();
			callContext.getReturnHandler().handleMessage(new Message());
		}
		// Intent i = new Intent(callContext,
		// EquipmentTreeViewListActivity.class);
		// callContext.startActivity(i);
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

		String line = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileToImport.getDataFile()
					.getFileToParse());
			reader = new CsvReader(fis, ';', set);
			reader.readHeaders();

			boolean skipRecord = false;
			publishProgress("initialisiere Header...");
			headLayer = reader.getIndex(COLUMN_LAYER);
			headOE = reader.getIndex(COLUMN_OE);
			headType = reader.getIndex(COLUMN_TYPE);
			headFB = reader.getIndex(COLUMN_FB);
			headQuantity = reader.getIndex(COLUMN_QUANTITY);
			headStock = reader.getIndex(COLUMN_STOCK);
			headActualQuantity = reader.getIndex(COLUMN_ACTUALQUANTITY);
			headDescription = reader.getIndex(COLUMN_DESCRIPTION);
			headEquipNo = reader.getIndex(COLUMN_EQUIP_NO);
			headInvNo = reader.getIndex(COLUMN_INV_NO);
			headDeviceNo = reader.getIndex(COLUMN_DEVICE_NO);
			headColumnStatus = reader.getIndex(COLUMN_STATUS);
			headColumnImage = reader.getIndex(COLUMN_IMAGE);
			
			Boolean hasImage = false;
			if(headColumnImage > -1)
			{
				readerImage = new CsvReader(new FileInputStream(fileToImport.getImageFile().getFileToParse()),';', set);
				readerImage.setSafetySwitch(false);
				readerImage.readHeaders();
				headId = readerImage.getIndex(COLUMN_ID);
				headImageStream = readerImage.getIndex(COLUMN_STREAM);
				hasImage = true;
			}

			// Equipment lastItem = loadNextElement(null);
			// CsvAsList = new Vector<Equipment>();
			Equipment lastItem = null;
			Equipment head = null;
			while (reader.readRecord()) {

					publishProgress("load RowNr:  " + rowCount++);
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
					if(hasImage)
					{
						strImageId = reader.get(headColumnImage).trim();
					}
					

					Equipment currentData = new Equipment();
					currentData.setLayer(parseInt(strLayer));
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
					
					if(!strImageId.isEmpty())
					{
						while(readerImage.readRecord())
						{
							if(readerImage.get(headId).equals(String.valueOf(strImageId)))
							{
								String strStream = readerImage.get(headImageStream);
								EquipmentImage currentImage = new EquipmentImage();
								byte[] imgBytes = Base64.decode(strStream, 0);
								currentImage.setImgBytes(imgBytes);
								currentData.setEquipImg(currentImage);
								break;
							}
						}
					}

					// CsvAsList.add(currentData);

					// head wurde gefunden... Initialisiere ihn...
					if (head == null) {
						currentData.setLayer(0);
						currentData.setParent(null);
						head = currentData;
						lastItem = head;
					} else {

						if (currentData.getLayer() > lastItem.getLayer()) {
							currentData.setParent(lastItem);
							lastItem.getChilds().add(currentData);
						} else if (currentData.getLayer() == lastItem
								.getLayer()) {
							currentData.setParent(lastItem.getParent());
							lastItem.getParent().getChilds().add(currentData);
						} else if (currentData.getLayer() < lastItem.getLayer()) {
							Equipment nextSibling = lastItem;
							while (currentData.getLayer() != nextSibling
									.getLayer()) {
								nextSibling = nextSibling.getParent();
							}
							currentData.setParent(nextSibling.getParent());
							nextSibling.getParent().getChilds()
									.add(currentData);
						}
						
					}
					lastItem = currentData;
					dbHelper.getDbHelperEquip().createOrUpdateEquipment(currentData);
			}
			
			
			//save head mit alen childs to DB
			//publishProgress("save Data to DB...");
			//dbHelper.getDbHelperEquip().createOrUpdateEquipment(head);
			
			
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

	/*
	 * private void ListToTree() { Equipment head = CsvAsList.get(0); head =
	 * loadNextElement(head); }
	 * 
	 * private int index = 0; private Equipment loadNextElement(Equipment
	 * parent) { index++; if(index < CsvAsList.size()) { if(parent.getLayer() !=
	 * 0) { parent.getChilds().add(CsvAsList.get(index)); }
	 * 
	 * 
	 * if(CsvAsList.get(index).getLayer() == parent.getLayer()) {
	 * loadNextElement(parent); } else { loadNextElement(CsvAsList.get(index));
	 * } }
	 * 
	 * return parent; }
	 */

	/*
	 * private Equipment loadNextElement(Equipment parent) throws IOException {
	 * reader.readRecord();
	 * 
	 * publishProgress("load RowNr:  " + rowCount++); String strLayer =
	 * reader.get(headLayer).trim(); String strOE = reader.get(headOE).trim();
	 * String strType = reader.get(headType).trim(); String strFB =
	 * reader.get(headFB).trim(); String strQuantity =
	 * reader.get(headQuantity).trim(); String strStock =
	 * reader.get(headStock).trim(); String strActualQuantity =
	 * reader.get(headActualQuantity) .trim(); String strDescription =
	 * reader.get(headDescription).trim(); String strEquipNo =
	 * reader.get(headEquipNo).trim(); String strInvNo =
	 * reader.get(headInvNo).trim(); String strDeviceNo =
	 * reader.get(headDeviceNo).trim(); String strStatus =
	 * reader.get(headColumnStatus).trim();
	 * 
	 * Equipment currentData = new Equipment();
	 * currentData.setLayer(parseInt(strLayer)); currentData.setLocation(strOE);
	 * currentData.setType(getTypeFromString(strType));
	 * currentData.setForeignPart(parseBool(strFB));
	 * currentData.setTargetQuantity(parseInt(strActualQuantity));
	 * currentData.setStock(parseInt(strStock));
	 * currentData.setActualQuantity(parseInt(strQuantity));
	 * currentData.setDescription(strDescription);
	 * currentData.setEquipNo(strEquipNo); currentData.setInvNo(strInvNo);
	 * currentData.setDeviceNo(strDeviceNo);
	 * currentData.setStatus(getStatusFromString(strStatus));
	 * 
	 * if(parent == null) { parent = currentData; parent.setLayer(0); }
	 * 
	 * 
	 * int currentLayer = currentData.getLayer();
	 * 
	 * if(currentLayer != 0) parent.getChilds().add(currentData);
	 * 
	 * if(currentLayer == parent.getLayer()) { loadNextElement(parent); } else {
	 * loadNextElement(currentData); }
	 * 
	 * if(currentLayer == 0) return parent;
	 * 
	 * return null; }
	 */
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
			} else {
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
