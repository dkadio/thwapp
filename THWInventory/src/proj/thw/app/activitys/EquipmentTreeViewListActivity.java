package proj.thw.app.activitys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import proj.thw.app.R;
import proj.thw.app.adapters.ThwTreeViewAdapter;
import proj.thw.app.classes.Equipment;
import proj.thw.app.treeview.InMemoryTreeStateManager;
import proj.thw.app.treeview.TreeBuilder;
import proj.thw.app.treeview.TreeStateManager;
import proj.thw.app.treeview.TreeViewList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Demo activity showing how the tree view can be used.
 * 
 */
public class EquipmentTreeViewListActivity extends Activity {

	 private static final int[] DEMO_NODES = new int[] { 0, 0, 1, 1, 1, 2, 2, 1,
         1, 2, 1, 0, 0, 0, 1, 2, 3, 2, 0, 0, 1, 2, 0, 1, 2, 0, 1 };
	
    private final Set<Equipment> selected = new HashSet<Equipment>();
    private TreeViewList tvlEquipment;

   
    private static final int LEVEL_NUMBER = 6;
    private TreeStateManager<Equipment> manager = null;
    private ThwTreeViewAdapter simpleAdapter;
    private boolean collapsible;
    
    private ArrayList<Equipment> equipmentList;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      

        boolean newCollapsible;
        if (savedInstanceState == null) {
            manager = new InMemoryTreeStateManager<Equipment>();
            final TreeBuilder<Equipment> treeBuilder = new TreeBuilder<Equipment>(manager);
            for (int i = 0; i < equipmentList.size(); i++) {
                treeBuilder.sequentiallyAddNextNode(equipmentList.get(i),equipmentList.get(i).getLayer() -1);
                Log.i("", "add: " + i);
            }
            newCollapsible = true;
            
        } else {
            manager = (TreeStateManager<Equipment>) savedInstanceState
                    .getSerializable("treeManager");
            if (manager == null) {
                manager = new InMemoryTreeStateManager<Equipment>();
            }
            newCollapsible = savedInstanceState.getBoolean("collapsible");
        }
       
        
        
        setContentView(R.layout.activity_tree_view_list);
        tvlEquipment = (TreeViewList) findViewById(R.id.tvlequip);
     
        simpleAdapter = new ThwTreeViewAdapter(this, selected, manager,
                LEVEL_NUMBER);
        
        tvlEquipment.setAdapter(simpleAdapter);
        setCollapsible(newCollapsible);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
		case R.id.action_settings:
	    	Intent intent = new Intent(this, DetailListActivity.class);
	    	startActivity(intent);
			break;

		default:
			break;
		}
    	

return super.onOptionsItemSelected(item);
    }

    /*
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
    }*/
}