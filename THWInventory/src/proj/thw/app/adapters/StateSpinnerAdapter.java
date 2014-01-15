package proj.thw.app.adapters;

import java.util.Vector;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class StateSpinnerAdapter extends BaseAdapter {

	private LayoutInflater inflator;
	private Context context;
	Vector<Equipment.Status> states;

	public StateSpinnerAdapter(Context context, Vector<Equipment.Status> states) {
		this.states = states;
		this.context = context;
	}

	@Override
	public int getCount() {
		return states.size();
	}

	@Override
	public Object getItem(int position) {

		return states.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView spinner;
		CheckBox cb;
		if (v == null) {
			v = inflator.inflate(R.layout.spinner_item, parent, false);
		}
		cb = (CheckBox) v.findViewById(R.id.checkBox);
		spinner = (TextView) v.findViewById(R.id.spinnerTextview);

		return v;
	}

}
