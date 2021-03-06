package proj.thw.app.adapters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import proj.thw.app.R;
import proj.thw.app.activitys.EquipmentTreeViewListActivity;
import proj.thw.app.classes.Equipment;
import proj.thw.app.treeview.AbstractTreeViewAdapter;
import proj.thw.app.treeview.TreeNodeInfo;
import proj.thw.app.treeview.TreeStateManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Klasse, die einen Adapter fuer die TreeListView darstellt, um Equipmentojekte in einer Baumstruktur 
 * darzustellen
 * @author max / deniz
 *
 */
public class ThwTreeViewAdapter extends AbstractTreeViewAdapter<Equipment> {

	private Set<Equipment> selected;

	private final OnCheckedChangeListener onCheckedChange = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(final CompoundButton buttonView,
				final boolean isChecked) {
			final Equipment id = (Equipment) buttonView.getTag();
			changeSelected(isChecked, id);
		}

	};

	private void changeSelected(final boolean isChecked, final Equipment id) {
		if (isChecked) {
			selected.add(id);
		} else {
			selected.remove(id);
		}
	}

	public ThwTreeViewAdapter(final EquipmentTreeViewListActivity treeViewList,
			Set<Equipment> selected,
			final TreeStateManager<Equipment> treeStateManager,
			final int numberOfLevels) {
		super(treeViewList, treeStateManager, numberOfLevels);
		if (selected == null) {
			this.selected = new HashSet<Equipment>();
		} else {
			this.selected = selected;
		}

	}

	private String getDescription(final Equipment id) {
		final Integer[] hierarchy = getManager().getHierarchyDescription(id);
		return id.toString(); // + Arrays.asList(hierarchy);
	}

	@Override
	public View getNewChildView(final TreeNodeInfo<Equipment> treeNodeInfo) {
		final LinearLayout viewLayout = (LinearLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.tree_list_item, null);
		return updateView(viewLayout, treeNodeInfo);
	}

	@Override
	public LinearLayout updateView(final View view,
			final TreeNodeInfo<Equipment> treeNodeInfo) {
		final LinearLayout viewLayout = (LinearLayout) view;
		final TextView descriptionView = (TextView) viewLayout
				.findViewById(R.id.tree_list_item_description);
		final TextView levelView = (TextView) viewLayout
				.findViewById(R.id.tree_list_item_level);
		ImageView itemImage = (ImageView) viewLayout
				.findViewById(R.id.treeitemimg);
		itemImage.setImageBitmap(null);
		descriptionView.setText(getDescription(treeNodeInfo.getId()));
		levelView.setText(Integer.toString(treeNodeInfo.getLevel()));
		if (treeNodeInfo.getId().getEquipImg().getImg() != null)
		{
			itemImage.setImageBitmap(treeNodeInfo.getId().getEquipImg()
					.getImg());
		}
		else
		{
			itemImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.error));
		}

		final CheckBox box = (CheckBox) viewLayout
				.findViewById(R.id.tree_list_checkbox);
		box.setTag(treeNodeInfo.getId());
		if (treeNodeInfo.isWithChildren()) {
			box.setVisibility(View.GONE);
		} else {
			box.setVisibility(View.VISIBLE);
			box.setChecked(selected.contains(treeNodeInfo.getId()));
		}
		box.setOnCheckedChangeListener(onCheckedChange);
		return viewLayout;
	}

	@Override
	public void handleItemClick(final View view, final Object id) {
		final Equipment longId = (Equipment) id;
		final TreeNodeInfo<Equipment> info = getManager().getNodeInfo(longId);
		if (info.isWithChildren()) {
			super.handleItemClick(view, id);
		} else {
			final ViewGroup vg = (ViewGroup) view;
			final CheckBox cb = (CheckBox) vg
					.findViewById(R.id.tree_list_checkbox);
			//cb.performClick();
		}
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public Set<Equipment> getSelected() {
		return selected;
		}

	@Override
	public void updateItem(View view, Equipment udpItem) {
		//funktioniert nicht, da die Instanz von einer anderen Activity geandert wird...
		boolean hasChild = false;
		List<Equipment> childList = getManager().getChildren(udpItem);
		if(childList.size() > 0)
			hasChild = true;
		
		TreeNodeInfo<Equipment> udpNodeInfo = new TreeNodeInfo<Equipment>(udpItem, udpItem.getLayer() -1,hasChild, true, true);
		updateView(view, udpNodeInfo);
		refresh();
		
	}

}
