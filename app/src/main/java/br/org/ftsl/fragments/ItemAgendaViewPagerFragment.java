package br.org.ftsl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.org.ftsl.activities.ItemGridDetail;
import br.org.ftsl.adapter.ItemGridAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.model.TypeModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;

public class ItemAgendaViewPagerFragment extends Fragment{

    private Integer mDay;

    private ListView mListViewItems;
    private DatabaseHelper mDatabaseHelper;

    public static ItemAgendaViewPagerFragment newInstance(Integer day) {
        ItemAgendaViewPagerFragment fragment = new ItemAgendaViewPagerFragment();
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

                ItemGridModel itemGrade = (ItemGridModel) mListViewItems.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ItemGridDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(Constant.ITEM_GRID, itemGrade);
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
    }

    private void fillGrid(){

        if(mListViewItems != null) {

            List<ItemGridModel> items = mDatabaseHelper.getItemsGrid(mDay, 0, true);

            Map<Integer, String> types = new HashMap<>();
            for(TypeModel type : mDatabaseHelper.getTypeDao().queryForAll()) {
                types.put(type.getId(), type.getType());
            }

            ItemGridAdapter adapter = new ItemGridAdapter(getActivity().getApplicationContext(), items, true, types);
            mListViewItems.setAdapter(adapter);

        }

    }

}


