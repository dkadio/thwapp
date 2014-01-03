package proj.thw.app.activity;

import proj.thw.app.classes.ThwTreeViewAdapter;
import proj.thw.app.treeview.InMemoryTreeStateManager;
import proj.thw.app.treeview.TreeBuilder;
import proj.thw.app.treeview.TreeStateManager;
import proj.thw.app.treeview.TreeViewList;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class EquipmentTreeViewList extends Activity {

	
	 private static final int[] DEMO_NODES = new int[] { 0, 0, 1, 1, 1, 2, 2, 1,
         1, 2, 1, 0, 0, 0, 1, 2, 3, 2, 0, 0, 1, 2, 0, 1, 2, 0, 1 };
	//Views
	private TreeViewList tvlEquipment; 
	
	//treeViewList Obejcts
	private TreeStateManager<Long> manager;
	private ThwTreeViewAdapter simpleAdapter;
    private boolean collapsible;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvlEquipment = (TreeViewList) findViewById(R.id.tvlequip);
		
        boolean newCollapsible;
        if (savedInstanceState == null) {
            manager = new InMemoryTreeStateManager<Long>();
            final TreeBuilder<Long> treeBuilder = new TreeBuilder<Long>(manager);
            for (int i = 0; i < DEMO_NODES.length; i++) {
                treeBuilder.sequentiallyAddNextNode((long) i, DEMO_NODES[i]);
            }  
            newCollapsible = true;
        } else {
    
            manager = (TreeStateManager<Long>) savedInstanceState.getSerializable("treeManager");
            if (manager == null) {
                manager = new InMemoryTreeStateManager<Long>();
            }
            newCollapsible = savedInstanceState.getBoolean("collapsible");
        }
		
        setCollapsible(newCollapsible);
        tvlEquipment.setAdapter(simpleAdapter);
        registerForContextMenu(tvlEquipment);
	}
	
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("treeManager", manager);
        outState.putBoolean("collapsible", this.collapsible);
        super.onSaveInstanceState(outState);
    }
	
	 protected final void setCollapsible(final boolean newCollapsible) {
	    this.collapsible = newCollapsible;
	    tvlEquipment.setCollapsible(this.collapsible);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 @Override
	    public boolean onContextItemSelected(final MenuItem item) {
	        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	                .getMenuInfo();
	        final long id = info.id;
	        if (item.getItemId() == R.id.context_menu_collapse) {
	            manager.collapseChildren(id);
	            return true;
	        } else if (item.getItemId() == R.id.context_menu_expand_all) {
	            manager.expandEverythingBelow(id);
	            return true;
	        } else if (item.getItemId() == R.id.context_menu_expand_item) {
	            manager.expandDirectChildren(id);
	            return true;
	        } else if (item.getItemId() == R.id.context_menu_delete) {
	            manager.removeNodeRecursively(id);
	            return true;
	        } else {
	            return super.onContextItemSelected(item);
	        }
	    }

}
