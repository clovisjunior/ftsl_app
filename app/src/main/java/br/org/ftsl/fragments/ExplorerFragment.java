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

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.AuthorModel;
import br.org.ftsl.model.ItemGridModel;
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

        ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

        if(actionBar != null) {
            SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(
                    this.getActivity().getApplicationContext(),
                    R.array.event_types_items,
                    android.R.layout.simple_spinner_dropdown_item);

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

        //generateFakes();
    }


    private void generateFakes(){
        criarItemGradeFakePassado(10);
        criarItemGradeFakePassado(20);
        criarItemGradeFakePassado(30);
        criarItemGradeFakePassado(40);
        criarItemGradeFakePassado(50);

        criarItemGradeFake(1, 0);
        criarItemGradeFake(1, 5);
        criarItemGradeFake(1, 10);


        List<ItemGridModel> agenda = mDatabaseHelper.getAgenda();
        rescheduleAlarm(agenda);

    }

    private void criarItemGradeFakePassado(int minutos) {

        String titulo = "Evento de Teste Passado - ";

        //Teste de AlarmManager
        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.DAY_OF_MONTH, inicio.get(Calendar.DAY_OF_MONTH) - 1);
        inicio.set(Calendar.HOUR_OF_DAY, 9);
        inicio.set(Calendar.MINUTE, 5 + minutos);
        inicio.set(Calendar.SECOND, 0);

        Calendar fim = Calendar.getInstance();
        fim.setTimeInMillis(inicio.getTimeInMillis());
        fim.add(Calendar.HOUR_OF_DAY, 1);

        ItemGridModel itemGrade = new ItemGridModel();
        itemGrade.setInicio(new Date(inicio.getTimeInMillis()));
        itemGrade.setFim(new Date(fim.getTimeInMillis()));
        //itemGrade.setIsAssistir(true);
        itemGrade.setPid(140);
        itemGrade.setTime(1);
        itemGrade.setDate(1);
        itemGrade.setPlace(1);
        itemGrade.setTitle(titulo + minutos);
        itemGrade.setType(1);

        AuthorModel author = new AuthorModel();
        author.setCurriculum("Biografia");
        author.setId(900);
        author.setName("Clovis Lemes Ferreira Junior");

        itemGrade.setAuthor(author);

        List<ItemGridModel> t = mDatabaseHelper.getItemGridDao().queryForEq("title", titulo + minutos);

        if(t == null || t.isEmpty()) {
            mDatabaseHelper.createItemGrid(itemGrade);
        }
    }

    private void criarItemGradeFake(int dia, int minutos) {

        String titulo = "Evento de Teste do Lembrete - ";

        //Teste de AlarmManager
        Calendar inicio = Calendar.getInstance();
        //inicio.set(Calendar.HOUR_OF_DAY, 16);
        //inicio.set(Calendar.DAY_OF_MONTH, dia);
        inicio.add(Calendar.MINUTE, 2 + minutos);
        inicio.set(Calendar.SECOND, 0);

        Calendar fim = Calendar.getInstance();
        fim.setTimeInMillis(inicio.getTimeInMillis());
        fim.add(Calendar.HOUR_OF_DAY, 1);

        ItemGridModel itemGrade = new ItemGridModel();
        itemGrade.setInicio(new Date(inicio.getTimeInMillis()));
        itemGrade.setFim(new Date(fim.getTimeInMillis()));

        itemGrade.setAssistir(true);
        itemGrade.setPid(140);
        itemGrade.setTime(1);
        itemGrade.setDate(1);
        itemGrade.setPlace(1);
        itemGrade.setTitle(titulo + minutos);
        itemGrade.setType(1);


        AuthorModel author = new AuthorModel();
        author.setCurriculum("Biografia");
        author.setId(900);
        author.setName("Clovis Lemes Ferreira Junior");

        itemGrade.setAuthor(author);

        List<ItemGridModel> t = mDatabaseHelper.getItemGridDao().queryForEq("title", titulo + minutos);

        if(t == null || t.isEmpty()) {
            mDatabaseHelper.createItemGrid(itemGrade);
        }
    }


    private void checkIfDatabaseExist() {
        if(mDatabaseHelper.getItemGridDao().queryForAll().isEmpty()){
            fillGrid();
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
            new GridTask(getActivity()).execute();
        }

    }

    private void rescheduleAlarm(List<ItemGridModel> agenda) {

        for(ItemGridModel item : agenda){
            Utils.removeAlarm(getActivity(), item);
            Utils.setAlarm(getActivity(), item);

            try {
                List<ItemGridModel> items = mDatabaseHelper.getItemGridByProposta(item.getPid());
                for(ItemGridModel itemGridModel : items) {
                    itemGridModel.setAssistir(Boolean.TRUE);
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

        public GridTask(Context context){
            this.mProgressDialog = new ProgressDialog(context);
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_aguarde));
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

                String url = Constant.URL_SERVICE;

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                return restTemplate.getForObject(url, ItemGridModel[].class);
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
                    item.setInicio(Utils.getTime(item.getDate(), item.getTime(), true));
                    item.setFim(Utils.getTime(item.getDate(), item.getTime(), false));
                    mDatabaseHelper.createItemGrid(item);
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

}