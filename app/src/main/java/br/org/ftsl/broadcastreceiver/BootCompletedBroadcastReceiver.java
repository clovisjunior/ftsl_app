package br.org.ftsl.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.utils.Utils;

/**
 * Created by 05081364908 on 29/07/14.
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        mDatabaseHelper = new DatabaseHelper(context);

        List<ItemGridModel> items = mDatabaseHelper.getAgenda();

        if(items != null) {
            for (ItemGridModel item : items) {
                Utils.setAlarm(context, item);
            }
        }

    }
}
