package br.org.ftsl.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.SimpleDateFormat;

import br.org.ftsl.activities.ItemGridDetail;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;

/**
 * Created by 05081364908 on 28/07/14.
 */
public class AgendaBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ItemGridModel itemGrade = (ItemGridModel) intent.getSerializableExtra(Constant.ITEM_GRID);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        String text = format.format(itemGrade.getStart()) + " - " + itemGrade.getTitle();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                                                .setContentTitle(context.getString(R.string.notification_title))
                                                .setSmallIcon(R.drawable.ic_launcher)
                                                .setDefaults(0)
                                                .setLights(0xff3eb1d8, 2000, 5000)
                                                .setVibrate(new long[]{1000, 1000, 1000})
                                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                                                .setAutoCancel(true);

        Intent resultIntent = new Intent(context, ItemGridDetail.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtra(Constant.ITEM_GRID, itemGrade);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ItemGridDetail.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(itemGrade.getId(), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(itemGrade.getId(), notification.build());

    }
}
