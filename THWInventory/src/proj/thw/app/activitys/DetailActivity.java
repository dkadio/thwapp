package proj.thw.app.activitys;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
