package proj.thw.app.treeview;

import android.view.View;
import android.widget.AdapterView;

public interface OnTreeViewListItemClickListener {

	public void onTreeViewListItemClick(final AdapterView< ? > parent,
            final View view, final int position, final long id);
}
