package proj.thw.app.activitys;

import java.sql.SQLException;
import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
	
	private static final String KEY_LOADED_LIST 	= "key.load.list";
	private static final String KEY_EQUIPMENT_LIST 	= "key..list";
	private static int SPLASH_TIME_OUT 				= 5000;
	private static TextView tvStatus;
	private static OrmDBHelper dbHelper;
	private static Context callContext;
	private static ProgressBar pbLoadDB;
	
	private static Handler postLoadDB = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			Bundle bnd = msg.getData();
			if(bnd.containsKey(KEY_LOADED_LIST))
			{
				ArrayList<Equipment> loadedList = (ArrayList<Equipment>)bnd.get(KEY_LOADED_LIST);
				Intent i = new Intent(callContext,EquipmentTreeViewListActivity.class);
				i.putExtra(KEY_EQUIPMENT_LIST, loadedList);
				callContext.startActivity(i);
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		callContext = this;
		/*
		//init dbHanlder 
		dbHelper = new OrmDBHelper(this);
		
		//init Views
		tvStatus = (TextView) findViewById(R.id.tvstatus);
		tvStatus.setVisibility(View.INVISIBLE);
		
		pbLoadDB = (ProgressBar) findViewById(R.id.pbloaddb);
		*/
		 new Handler().postDelayed(new Runnable() {
			 
	            @Override
	            public void run() {
	                
	            	/*
	            	//init Handler
	        		Thread loadDB = new Thread(new Runnable() {
	        			
	        			@Override
	        			public void run() {
	        				
	        				ArrayList<Equipment> loadedList;
	        				try {
	        					tvStatus.post(new Runnable() {
	        						
	        						@Override
	        						public void run() {
	        							pbLoadDB.setVisibility(View.VISIBLE);
	        							tvStatus.setText("Lade Datenbank...");
	        							
	        						}
	        					});
	        					loadedList = (ArrayList<Equipment>) dbHelper.getDbHelperEquip().selectAllEquipments();
	        					Bundle bnd = new Bundle();
	        					bnd.putSerializable(KEY_LOADED_LIST, loadedList);
	        					Message msg = new Message();
	        					msg.setData(bnd);
	        					postLoadDB.handleMessage(msg);
	        				} catch (SQLException e) {
	        					Log.e(this.getClass().getName(), e.getMessage());
	        					Toast.makeText(callContext, "Error beim Laden der Datenbank!", Toast.LENGTH_LONG).show();
	        					finish();
	        				}
	        			}
	        		});
	        		
	        		loadDB.start();
	        		*/
	            	
	            	Intent i = new Intent(callContext,EquipmentTreeViewListActivity.class);
	            	callContext.startActivity(i);
	            }
	        }, SPLASH_TIME_OUT);
		
		
	}
}
