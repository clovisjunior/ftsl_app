package br.org.ftsl.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.org.ftsl.adapter.ItemGridAdapter;
import br.org.ftsl.adapter.ViewPagerAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.DayModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.sliding.SamplePagerItem;
import br.org.ftsl.sliding.SlidingTabLayout;
import br.org.ftsl.utils.Constant;
import br.org.ftsl.utils.Menus;
import br.org.ftsl.utils.Utils;

public class ExplorerViewPagerFragment extends Fragment {

	private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
    private ViewPager mViewPager;
    private ItemGridViewPagerFragment[] mFragments;
    private SlidingTabLayout mSlidingTabLayout;
    private boolean mSearchCheck;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        Integer sessionType = 0;

        if(getArguments() != null) {
            sessionType = (Integer) getArguments().get(Constant.EXPLORER_SESSION_TYPE);
        }

        mFragments = new ItemGridViewPagerFragment[] {
                ItemGridViewPagerFragment.newInstance(1, sessionType),
                ItemGridViewPagerFragment.newInstance(2, sessionType),
                ItemGridViewPagerFragment.newInstance(3, sessionType)
        };

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMM");
        Integer position = 0;
        for(DayModel day : mDatabaseHelper.getDayDao().queryForAll()) {
            mTabs.add(new SamplePagerItem(position, dayFormat.format(day.getDay()), getResources().getColor(Utils.colors[0]), Color.GRAY, mFragments));
            position++;
        }

    }

    public ListView[] getListViews(){
        ListView[] listViews = new ListView[3];

        listViews[0] = mFragments[0].getListView();
        listViews[1] = mFragments[1].getListView();
        listViews[2] = mFragments[2].getListView();

        return listViews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.explore_viewpager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	mViewPager = (ViewPager) view.findViewById(R.id.mPager);
    	
    	mViewPager.setOffscreenPageLimit(3); 
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.mTabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.white));
        mSlidingTabLayout.setViewPager(mViewPager);

        List<Calendar> days = new ArrayList<>();
        for(DayModel day : mDatabaseHelper.getDayDao().queryForAll()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(day.getDay());
            days.add(calendar);
        }

        Utils.selectCurrentDay(days, mViewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        try {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(Menus.SEARCH));
            searchView.setQueryHint(this.getString(R.string.search));

            ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                    .setHintTextColor(getResources().getColor(R.color.white));
            searchView.setOnQueryTextListener(OnQuerySearchView);
        }
        catch(Exception e){

        }

        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == Menus.SEARCH){
           mSearchCheck = true;
       }

       return super.onOptionsItemSelected(item);
    }

    private SearchView.OnQueryTextListener OnQuerySearchView = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String arg0) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String arg0) {
            if (mSearchCheck){
                ListView[] listViews = getListViews();
                for(int i=0 ; i < listViews.length ; i++){
                    ListView listView = listViews[i];
                    ((ItemGridAdapter) listView.getAdapter()).getFilter().filter(arg0);
                }
            }

            return true;
        }
    };

}