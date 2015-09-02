package br.org.ftsl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.org.ftsl.activities.ItemGridDetail;
import br.org.ftsl.adapter.ItemGridAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;

public class ItemGridViewPagerFragment extends Fragment{

    private Integer mSessionType;
    private Integer mDay;

    private ListView mListViewItems;
    private DatabaseHelper mDatabaseHelper;

    public static ItemGridViewPagerFragment newInstance(Integer day, Integer sessionType) {
        ItemGridViewPagerFragment fragment = new ItemGridViewPagerFragment();
        fragment.mSessionType = sessionType;
        fragment.mDay = day;
        return fragment;
    }

    @Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.grid_fragment, container, false);

        mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        mListViewItems = (ListView) rootView.findViewById(R.id.grid_items);

        mListViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemGridModel itemGrid = (ItemGridModel) mListViewItems.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ItemGridDetail.class);
                intent.putExtra(Constant.ITEM_GRID, itemGrid);

                startActivity(intent);

                }
            });

		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));

		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();
        fillGrid();
        scrollToCurrentEvent();
    }

    private void scrollToCurrentEvent() {

        List<ItemGridModel> items = ((ItemGridAdapter) mListViewItems.getAdapter()).getItemsAll();

        for(int i = 0 ; i < items.size() ; i++){
            ItemGridModel item = items.get(i);
            if(item.getInicio().after(new Date(System.currentTimeMillis()))){
                mListViewItems.setSelectionFromTop(i, 0);
                break;
            }
        }
    }

    private void fillGrid(){

        if(mListViewItems != null) {

            List<ItemGridModel> items = mDatabaseHelper.getItemsGrid(mDay, mSessionType, false);

            ItemGridAdapter adapter = new ItemGridAdapter(getActivity().getApplicationContext(), items, false);
            mListViewItems.setAdapter(adapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    public ListView getListView(){
        return this.mListViewItems;
    }

}


