package proj.thw.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class SplashScreenActivity extends Activity {

	
	private static final int SPLASH_TIME_OUT = 30000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, EquipmentTreeViewList.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
	}

}