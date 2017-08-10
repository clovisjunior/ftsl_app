package br.org.ftsl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.DayModel;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;
import br.org.ftsl.utils.Menus;
import br.org.ftsl.utils.Utils;

/**
 * Created by 05081364908 on 14/07/14.
 */
public class ItemGridDetail extends ActionBarActivity {

    private ItemGridModel mItemGrid;

    private DatabaseHelper mDatabaseHelper;

    private TextView mTxtTitle;
    private TextView mTxtScheduleLocation;
    private TextView mTxtDescription;
    private TextView mTxtSpeaker;
    private LinearLayout mSpeakersList;
    private View mViewRuler1;
    private View mViewRuler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.item_grid_detail);

        mTxtTitle = (TextView) findViewById(R.id.event_title);
        mTxtScheduleLocation = (TextView) findViewById(R.id.event_schedule_location);
        mTxtDescription = (TextView) findViewById(R.id.event_description);
        mSpeakersList = (LinearLayout) findViewById(R.id.speakersList);
        mTxtSpeaker = (TextView) findViewById(R.id.txtSpeaker);
        mViewRuler1 = findViewById(R.id.viewRule1);
        mViewRuler2 = findViewById(R.id.viewRule2);

        mItemGrid = (ItemGridModel) getIntent().getExtras().getSerializable(Constant.ITEM_GRID);

        fillDetail();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void fillDetail(){

        StringBuilder sb = new StringBuilder();

        DayModel day = mDatabaseHelper.getDayDao().queryForId(mItemGrid.getDate());

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        sb.append(dayFormat.format(day.getDay()));
        sb.append(" às ");
        sb.append(format.format(mItemGrid.getStart()));
        sb.append(" - ");
        sb.append(format.format(mItemGrid.getEnd()));

        String placeDescription = mDatabaseHelper.getPlaceDescription(mItemGrid);

        if(!"".equals(placeDescription)) {
            sb.append(" em ");
            sb.append(mDatabaseHelper.getPlaceDescription(mItemGrid));
        }

        mTxtTitle.setText(Html.fromHtml(mItemGrid.getTitle()).toString());
        mTxtScheduleLocation.setText(sb.toString());

        if(mItemGrid.getDescription() != null) {
            mTxtDescription.setText(Html.fromHtml(mItemGrid.getDescription()).toString());
        }

        if(mItemGrid.getAuthor() != null) {

            mSpeakersList.removeAllViews();

            View viewAuthor = getLayoutInflater().inflate(R.layout.item_grid_detail_speakers, null);
            TextView txtName = (TextView) viewAuthor.findViewById(R.id.event_speaker);
            TextView txtResume = (TextView) viewAuthor.findViewById(R.id.event_speaker_resume);

            if("admin".equals(mItemGrid.getAuthor().getName())){
                txtName.setText("Organização");
                txtResume.setText("");
            }
            else {
                txtName.setText(mItemGrid.getAuthor().getName());
                txtResume.setText(mItemGrid.getAuthor().getCurriculum());
            }

            mSpeakersList.addView(viewAuthor);
        }

        mTxtSpeaker.setText(getString(R.string.event_detail_spekears));
        mViewRuler1.setVisibility(View.VISIBLE);
        mViewRuler2.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);

        if(mItemGrid.getStart().before(new Date(System.currentTimeMillis()))) {
            menu.findItem(R.id.menu_del_agenda).setVisible(false);
            menu.findItem(R.id.menu_add_agenda).setVisible(false);
        }
        else{
            if (mItemGrid.getIsWatch()) {
                menu.findItem(R.id.menu_del_agenda).setVisible(true);
                menu.findItem(R.id.menu_add_agenda).setVisible(false);
            } else {
                menu.findItem(R.id.menu_del_agenda).setVisible(false);
                menu.findItem(R.id.menu_add_agenda).setVisible(true);
            }
        }

        menu.findItem(R.id.menu_update).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case Menus.ADD_AGENDA:
                addAgenda();
                return true;
            case Menus.DEL_AGENDA:
                removeAgenda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, NavigationMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void removeAgenda() {
        mItemGrid.setIsWatch(Boolean.FALSE);
        mDatabaseHelper.getItemGridDao().update(mItemGrid);
        Toast.makeText(this, getString(R.string.agenda_remove_event), Toast.LENGTH_LONG).show();
        invalidateOptionsMenu();
    }

    private void addAgenda() {

        mItemGrid.setIsWatch(Boolean.TRUE);
        mDatabaseHelper.getItemGridDao().update(mItemGrid);
        Toast.makeText(this, getString(R.string.agenda_add_event), Toast.LENGTH_LONG).show();
        invalidateOptionsMenu();

        Utils.setAlarm(this, mItemGrid);
    }

    @Override
    public void onBackPressed() {
        navigateToHome();
    }

}
