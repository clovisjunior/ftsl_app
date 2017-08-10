package br.org.ftsl.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.org.ftsl.adapter.ViewPagerAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.DayModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.sliding.SamplePagerItem;
import br.org.ftsl.sliding.SlidingTabLayout;
import br.org.ftsl.utils.Utils;

public class MyAgendaViewPagerFragment extends Fragment {
	private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    private ViewPager mViewPager;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        Fragment[] fragments = new Fragment[] {
                ItemAgendaViewPagerFragment.newInstance(1),
                ItemAgendaViewPagerFragment.newInstance(2),
                ItemAgendaViewPagerFragment.newInstance(3)
        };

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMM");
        Integer position = 0;
        for(DayModel day : mDatabaseHelper.getDayDao().queryForAll()) {
            mTabs.add(new SamplePagerItem(position, dayFormat.format(day.getDay()), getResources().getColor(Utils.colors[0]), Color.GRAY, fragments));
            position++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_agenda_viewpager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	mViewPager = (ViewPager) view.findViewById(R.id.mPager);
    	
    	mViewPager.setOffscreenPageLimit(3); 
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.mTabs);
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

}