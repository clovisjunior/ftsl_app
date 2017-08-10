package br.org.ftsl.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import br.org.ftsl.activities.ZoomFunctionality;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;

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
