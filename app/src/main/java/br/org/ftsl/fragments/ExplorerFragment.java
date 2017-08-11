package br.org.ftsl.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.AboutModel;
import br.org.ftsl.model.AppConfiguration;
import br.org.ftsl.model.AuthorModel;
import br.org.ftsl.model.ClassRoomModel;
import br.org.ftsl.model.Configuration;
import br.org.ftsl.model.DayModel;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.model.TimeModel;
import br.org.ftsl.model.TypeModel;

import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;
import br.org.ftsl.utils.Menus;
import br.org.ftsl.utils.Utils;

public class ExplorerFragment extends Fragment{

    private DatabaseHelper mDatabaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createViewPager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        checkIfDatabaseExist();

        setHasOptionsMenu(true);

//        generateFakes();
    }

    private void makeActionBar() {
        ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

        if(actionBar != null) {

            List<String> types = new ArrayList<>();
            types.add(getResources().getString(R.string.event_type_all));
            for(TypeModel type : mDatabaseHelper.getTypeDao().queryForAll()){
                types.add(type.getType());
            }

            SpinnerAdapter mSpinnerAdapter = new ArrayAdapter( this.getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, types);


            actionBar.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener(){
                @Override
                public boolean onNavigationItemSelected(int position, long itemId) {

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getString(R.string.update));
                    progressDialog.show();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.EXPLORER_SESSION_TYPE, position);

                    ExplorerViewPagerFragment explorerViewPagerFragment = new ExplorerViewPagerFragment();
                    explorerViewPagerFragment.setArguments(bundle);

                    ft.replace(R.id.explorer_content, explorerViewPagerFragment);

                    ft.commit();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    return true;
                }
            });

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle("");
        }
    }


    private void checkIfDatabaseExist() {
        if(mDatabaseHelper.getItemGridDao().queryForAll().isEmpty()){
            fillGrid();
        }
        else {
            makeActionBar();
        }
    }

    private void createViewPager() {
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.explorer_content, new ExplorerViewPagerFragment()).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == Menus.UPDATE){
            fillGrid();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillGrid(){

        if (Utils.verifyInternetConnection(getActivity())) {
            new ConfigurationTask(getActivity()).execute();
        }

    }

    private void rescheduleAlarm(List<ItemGridModel> agenda) {

        for(ItemGridModel item : agenda){
            Utils.removeAlarm(getActivity(), item);
            Utils.setAlarm(getActivity(), item);

            try {
                List<ItemGridModel> items = mDatabaseHelper.getItemGridByProposta(item.getPid());
                for(ItemGridModel itemGridModel : items) {
                    itemGridModel.setIsWatch(Boolean.TRUE);
                    mDatabaseHelper.getItemGridDao().update(itemGridModel);
                }
            }
            catch(Exception e){

            }
        }

    }

    private class GridTask extends AsyncTask<Integer, Void, ItemGridModel[]> {

        private ProgressDialog mProgressDialog;
        private Context mContext;
        private String mGridUrl;

        public GridTask(Context context, String gridUrl){
            this.mProgressDialog = new ProgressDialog(context);
            this.mContext = context;
            this.mGridUrl = gridUrl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_waiting_grid));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ItemGridModel[] doInBackground(Integer... args) {

            try {

                RestTemplate restTemplate = new RestTemplate();

                HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
                httpRequestFactory.setConnectTimeout(10 * 1000);
                httpRequestFactory.setReadTimeout(10 * 1000);
                restTemplate.setRequestFactory(httpRequestFactory);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                return restTemplate.getForObject(mGridUrl, ItemGridModel[].class);
            }
            catch(ResourceAccessException e){
                Log.e(Constant.TAG, "Error update grid", e);
            }
            catch(Exception e){
                Log.e(Constant.TAG, "Error update grid", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ItemGridModel[] result) {

            if(result != null && result.length > 0) {

                List<ItemGridModel> items = Arrays.asList(result);

                List<ItemGridModel> agenda = mDatabaseHelper.getAgenda();

                mDatabaseHelper.removeAllItemGrid();

                for(ItemGridModel item : items){
                    item.setAuthor(new AuthorModel(item.getAuthorId(), item.getAuthorName(), item.getCurriculum()));
                    mDatabaseHelper.createItemGrid(item);
                }

                //Update times
                for(TimeModel time : mDatabaseHelper.getTimeDao().queryForAll()) {
                    mDatabaseHelper.updateItemGrid(time);
                }

                rescheduleAlarm(agenda);
            }
            else{
                Toast.makeText(mContext, getString(R.string.server_down), Toast.LENGTH_LONG).show();
            }

            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            createViewPager();
        }
    }

    private class ConfigurationTask extends AsyncTask<Integer, Void, Configuration> {

        private ProgressDialog mProgressDialog;
        private Context mContext;

        public ConfigurationTask(Context context){
            this.mProgressDialog = new ProgressDialog(context);
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_waiting_configuration));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Configuration doInBackground(Integer... args) {

            try {

                RestTemplate restTemplate = new RestTemplate();

                HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
                httpRequestFactory.setConnectTimeout(10 * 1000);
                httpRequestFactory.setReadTimeout(10 * 1000);

                restTemplate.setRequestFactory(httpRequestFactory);

                String url = Constant.URL_SERVICE_CONFIGURATION;

                restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
                String configString = restTemplate.getForObject(url, String.class);

                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(configString, Configuration.class);
            }
            catch(ResourceAccessException e){
                Log.e(Constant.TAG, "Error update configuration", e);
            }
            catch(Exception e){
                Log.e(Constant.TAG, "Error update configuration", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Configuration result) {

            if(result != null) {

            }
            else{
                Toast.makeText(mContext, getString(R.string.server_down), Toast.LENGTH_LONG).show();
            }

            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if(result != null) {
                new AppConfigurationTask(getActivity(), result).execute();
            }

        }
    }

    private class AppConfigurationTask extends AsyncTask<Integer, Void, AppConfiguration> {

        private ProgressDialog mProgressDialog;
        private Context mContext;
        private Configuration mConfiguration;

        public AppConfigurationTask(Context context, Configuration configuration){
            this.mProgressDialog = new ProgressDialog(context);
            this.mContext = context;
            this.mConfiguration = configuration;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_waiting_configuration));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected AppConfiguration doInBackground(Integer... args) {

            try {

                RestTemplate restTemplate = new RestTemplate();

                HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
                httpRequestFactory.setConnectTimeout(10 * 1000);
                httpRequestFactory.setReadTimeout(10 * 1000);

                restTemplate.setRequestFactory(httpRequestFactory);

                restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
                String configString = restTemplate.getForObject(mConfiguration.getConfig(), String.class);

                ObjectMapper mapper = new ObjectMapper();
                mapper.setDateFormat(new SimpleDateFormat(("dd/MM/yyyy")));

                return mapper.readValue(configString, AppConfiguration.class);
            }
            catch(ResourceAccessException e){
                Log.e(Constant.TAG, "Error update configuration", e);
            }
            catch(Exception e){
                Log.e(Constant.TAG, "Error update configuration", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(AppConfiguration result) {

            if(result != null) {

                mDatabaseHelper.removeAllClassRoom();

                // Rooms
                if(result.getRooms() != null && !result.getRooms().isEmpty()) {
                    for(ClassRoomModel room : result.getRooms()) {
                        mDatabaseHelper.createClassRoom(room);
                    }
                }

                // Labs
                if(result.getLabs() != null && !result.getLabs().isEmpty()) {
                    for(ClassRoomModel lab : result.getLabs()) {
                        mDatabaseHelper.createClassRoom(lab);
                    }
                }

                // Days
                if(result.getDays() != null && !result.getDays().isEmpty()) {
                    mDatabaseHelper.removeAllDays();
                    for(DayModel day : result.getDays()) {
                        mDatabaseHelper.createDay(day);
                    }
                }

                // Times
                if(result.getTimes() != null && !result.getTimes().isEmpty()) {
                    mDatabaseHelper.removeAllTimes();
                    for(TimeModel time : result.getTimes()) {
                        mDatabaseHelper.createTime(time);
                    }
                }

                // Types
                if(result.getTypes() != null && !result.getTypes().isEmpty()) {
                    mDatabaseHelper.removeAllTypes();
                    for(TypeModel type : result.getTypes()) {
                        mDatabaseHelper.createType(type);
                    }

                    makeActionBar();
                }

                // About
                if(result.getAbout() != null) {
                    mDatabaseHelper.removeAbout();
                    AboutModel about = new AboutModel();
                    about.setEvent(result.getAbout());
                    mDatabaseHelper.createAbout(about);
                }

            }
            else{
                Toast.makeText(mContext, getString(R.string.server_down), Toast.LENGTH_LONG).show();
            }

            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if(result != null) {
                new GridTask(getActivity(), mConfiguration.getPath()).execute();
            }

        }
    }

    private void generateFakes(){
        createItemGradeFakePass(10);
        createItemGradeFakePass(20);
        createItemGradeFakePass(30);
        createItemGradeFakePass(40);
        createItemGradeFakePass(50);

        createItemGradeFake(1, 10);
        createItemGradeFake(1, 15);
        createItemGradeFake(1, 20);


        List<ItemGridModel> agenda = mDatabaseHelper.getAgenda();
        rescheduleAlarm(agenda);

    }

    private void createItemGradeFakePass(int minutes) {

        String titulo = "Evento de Teste Passado - ";

        //Teste de AlarmManager
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 1);
        start.set(Calendar.HOUR_OF_DAY, 9);
        start.set(Calendar.MINUTE, 5 + minutes);
        start.set(Calendar.SECOND, 0);

        Calendar fim = Calendar.getInstance();
        fim.setTimeInMillis(start.getTimeInMillis());
        fim.add(Calendar.HOUR_OF_DAY, 1);

        ItemGridModel itemGrade = new ItemGridModel();
        itemGrade.setStart(new Date(start.getTimeInMillis()));
        itemGrade.setEnd(new Date(fim.getTimeInMillis()));
        itemGrade.setIsWatch(true);
        itemGrade.setPid(140);
        itemGrade.setTime(1);
        itemGrade.setDate(1);
        itemGrade.setPlace(1);
        itemGrade.setTitle(titulo + minutes);
        itemGrade.setType(1);

        AuthorModel author = new AuthorModel();
        author.setCurriculum("Biografia");
        author.setId(900);
        author.setName("Clovis Lemes Ferreira Junior");

        itemGrade.setAuthor(author);

        List<ItemGridModel> t = mDatabaseHelper.getItemGridDao().queryForEq("title", titulo + minutes);

        if(t == null || t.isEmpty()) {
            mDatabaseHelper.createItemGrid(itemGrade);
        }
    }

    private void createItemGradeFake(int day, int minute) {

        String titulo = "Evento de Teste do Lembrete - ";

        //Teste de AlarmManager
        Calendar start = Calendar.getInstance();
        //start.set(Calendar.HOUR_OF_DAY, 15);
        //start.set(Calendar.DAY_OF_MONTH, day);
        start.add(Calendar.MINUTE, 2 + minute);
        start.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis());
        end.add(Calendar.HOUR_OF_DAY, 1);

        ItemGridModel itemGrade = new ItemGridModel();
        itemGrade.setStart(new Date(start.getTimeInMillis()));
        itemGrade.setEnd(new Date(end.getTimeInMillis()));

        itemGrade.setIsWatch(true);
        itemGrade.setPid(140);
        itemGrade.setTime(1);
        itemGrade.setDate(1);
        itemGrade.setPlace(1);
        itemGrade.setTitle(titulo + minute);
        itemGrade.setType(1);

        AuthorModel author = new AuthorModel();
        author.setCurriculum("Biografia");
        author.setId(900);
        author.setName("Clovis Lemes Ferreira Junior");

        itemGrade.setAuthor(author);

        List<ItemGridModel> t = mDatabaseHelper.getItemGridDao().queryForEq("title", titulo + minute);

        if(t == null || t.isEmpty()) {
            mDatabaseHelper.createItemGrid(itemGrade);
        }
    }

}