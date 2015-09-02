
package br.org.ftsl.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.org.ftsl.adapter.NavigationAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.fragments.AboutFragment;
import br.org.ftsl.fragments.ExplorerFragment;
import br.org.ftsl.fragments.MapaFragment;
import br.org.ftsl.fragments.MyAgendaFragment;
import br.org.ftsl.navigation.NavigationList;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;
import br.org.ftsl.utils.Menus;

public class NavigationMain extends ActionBarActivity {
		
    private int mLastPosition = 0;
	private ListView mListDrawer;
	private DrawerLayout mLayoutDrawer;

	private NavigationAdapter mNavigationAdapter;
	private ActionBarDrawerToggleCompat mDrawerToggle;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");

		setContentView(R.layout.navigation_main);
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mListDrawer = (ListView) findViewById(R.id.listDrawer);
    	mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);

		if (mListDrawer != null) {
			mNavigationAdapter = NavigationList.getNavigationAdapter(this);
		}
		
		mListDrawer.setAdapter(mNavigationAdapter);
		mListDrawer.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggleCompat(this, mLayoutDrawer);
		mLayoutDrawer.setDrawerListener(mDrawerToggle);

		if (savedInstanceState != null) {
			setLastPosition(savedInstanceState.getInt(Constant.LAST_POSITION));
			
			if (mLastPosition < 4){
				mNavigationAdapter.resetCheck();
				mNavigationAdapter.setChecked(mLastPosition, true);
			}    	
			
	    }
        else{
            setLastPosition(mLastPosition);
            setFragmentList(mLastPosition);
        }

	}

    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
		outState.putInt(Constant.LAST_POSITION, mLastPosition);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {  

        if (mDrawerToggle.onOptionsItemSelected(item)) {
              return true;
        }		
        
		switch (item.getItemId()) {		
		case Menus.HOME:
			if (mLayoutDrawer.isDrawerOpen(mListDrawer)) {
				mLayoutDrawer.closeDrawer(mListDrawer);
			} else {
				mLayoutDrawer.openDrawer(mListDrawer);
			}
			return true;			
		default:
			return super.onOptionsItemSelected(item);			
		}		             
    }
		
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	hideMenus(menu, mLastPosition);
        return super.onPrepareOptionsMenu(menu);  
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);        		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);	     
	    mDrawerToggle.syncState();
	 }	
	
	public void setLastPosition(int position){
		this.mLastPosition = position;
	}

    public int getAgendaSize() {

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        int count = mDatabaseHelper.getAgenda().size();
        mDatabaseHelper.close();

        return count;
    }

    private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

		public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
			super(
			    mActivity,
			    mDrawerLayout, 
  			    R.drawable.ic_action_navigation_drawer, 
				R.string.drawer_open,
				R.string.drawer_close);
		}
		
		@Override
		public void onDrawerClosed(View view) {			
			supportInvalidateOptionsMenu();				
		}

		@Override
		public void onDrawerOpened(View drawerView) {	
			mNavigationAdapter.notifyDataSetChanged();
			supportInvalidateOptionsMenu();			
		}		
	}
		  
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {          	        	
	    	setLastPosition(position);
	    	setFragmentList(mLastPosition);
	    	mLayoutDrawer.closeDrawer(mListDrawer);
        }
    }	
    
	private void setFragmentList(int position){
		
		FragmentManager fragmentManager = getSupportFragmentManager();							
		
		switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ExplorerFragment()).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MyAgendaFragment()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MapaFragment()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
                break;
		}			
	
		if (position < 4){
			mNavigationAdapter.resetCheck();
			mNavigationAdapter.setChecked(position, true);
		}
	}

    private void hideMenus(Menu menu, int position) {

        boolean drawerOpen = mLayoutDrawer.isDrawerOpen(mListDrawer);

        switch (position) {
            case 0:
                menu.findItem(Menus.UPDATE).setVisible(!drawerOpen);
                menu.findItem(Menus.SEARCH).setVisible(!drawerOpen);
                break;

            case 1:
                //menu.findItem(Menus.UPDATE).setVisible(false);
                //menu.findItem(Menus.SEARCH).setVisible(false);
                break;
        }

    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
