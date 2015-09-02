package br.org.ftsl.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.org.ftsl.navigation.R;

public class MyAgendaFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_agenda_fragment, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.my_agenda_content, new MyAgendaViewPagerFragment()).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

        if(actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.my_agenda);
        }

    }
	
}
