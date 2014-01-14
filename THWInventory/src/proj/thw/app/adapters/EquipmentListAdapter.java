package proj.thw.app.adapters;

import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.classes.Equipment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquipmentListAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Equipment> equipments;
	private LayoutInflater inflator;
	
	public EquipmentListAdapter(Context context, ArrayList<Equipment> equipments) {
		this.context = context;
		this.equipments = equipments;
		inflator = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return equipments.size();
	}

	@Override
	public Object getItem(int position) {
		return equipments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView txtview;
		ImageView equipPicture;
		ImageView equipTODO;

		if (v == null) {
			v = inflator.inflate(R.layout.equipment_listview, parent);
		}

		txtview = (TextView) v.findViewById(R.id.equipdesc);
		equipPicture = (ImageView) v.findViewById(R.id.equipPicture);
		equipTODO = (ImageView) v.findViewById(R.id.equipTODO);
		
		txtview.setText(equipments.get(position).getDescription());
		//TODO equiPicutre implementieren
		Equipment e = equipments.get(position);
		if(e.getDeviceNo().isEmpty() || e.getInvNo().isEmpty() || e.getDeviceNo().isEmpty() || e.getStock() < e.getActualQuantity()){
			equipTODO.setVisibility(ImageView.VISIBLE);
		}else{
			equipTODO.setVisibility(ImageView.INVISIBLE);
		}
		
		return v;
	}

}
