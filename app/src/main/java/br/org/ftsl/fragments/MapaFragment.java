package br.org.ftsl.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import br.org.ftsl.activities.ZoomFunctionality;
import br.org.ftsl.navigation.R;

/**
 * Created by 05081364908 on 14/07/14.
 */
public class MapaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapa_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

        if(actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.map);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getView().setBackgroundColor(getResources().getColor(R.color.white));

        ZoomFunctionality zoomFunctionality = (ZoomFunctionality) view.findViewById(R.id.map_event);
        zoomFunctionality.setMaxZoom(4f);

        Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.map_event);
        zoomFunctionality.setImageBitmap(bmp);

    }

}
