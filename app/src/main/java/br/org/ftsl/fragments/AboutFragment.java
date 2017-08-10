package br.org.ftsl.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.AboutModel;
import br.org.ftsl.navigation.R;

public class AboutFragment extends Fragment {

    private DatabaseHelper mDatabaseHelper;

    private TextView mAbout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, container, false);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());


        ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

        if(actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.about);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setBackgroundColor(getResources().getColor(R.color.white));

        mAbout = (TextView) getView().findViewById(R.id.about_event_text);
        AboutModel about = mDatabaseHelper.getAboutDao().queryForAll().get(0);
        mAbout.setText(Html.fromHtml(about.getEvent()).toString());
    }
}
