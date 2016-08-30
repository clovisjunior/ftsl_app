package br.org.ftsl.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;


public class Utils {

	//Set all the navigation icons and always to set "zero 0" for the item is a category
	public static int[] iconNavigation = new int[] { 
		R.drawable.explore, R.drawable.agenda, R.drawable.map, R.drawable.about};
	
	public static int[] colors = new int[] {
		R.color.blue_dark, R.color.blue_dark, R.color.red_dark, R.color.red_light,
		R.color.green_dark, R.color.green_light, R.color.orange_dark, R.color.orange_light,
		R.color.purple_dark, R.color.purple_light};

    public static boolean verifyInternetConnection(Context context){

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {
            Toast.makeText(context, R.string.connection_down, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void setAlarm(Context context, ItemGridModel itemGrade){
        if(itemGrade.getInicio().after(new Date(System.currentTimeMillis()))) {

            //Register
            Intent intent = new Intent(Constant.AGENDA_BROADCAST_RECEIVER);
            intent.putExtra(Constant.ITEM_GRID, itemGrade);
            PendingIntent p = PendingIntent.getBroadcast(context, itemGrade.getId(), intent, 0);

            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(itemGrade.getInicio().getTime());
            c.add(Calendar.MINUTE, -10);
            c.set(Calendar.SECOND, 0);

            alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), p);
        }
    }

    public static void removeAlarm(Context context, ItemGridModel itemGrade){

        Intent intent = new Intent(Constant.AGENDA_BROADCAST_RECEIVER);
        intent.putExtra(Constant.ITEM_GRID, itemGrade);
        PendingIntent p = PendingIntent.getBroadcast(context, itemGrade.getId(), intent, 0);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(p);
    }

    public static void selectCurrentDay(ViewPager mViewPager) {
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.MONTH, Calendar.AUGUST);
        firstDay.set(Calendar.YEAR, 2016);
        firstDay.set(Calendar.DAY_OF_MONTH, 31);

        Calendar secondDay = Calendar.getInstance();
        secondDay.set(Calendar.MONTH, Calendar.SEPTEMBER);
        secondDay.set(Calendar.YEAR, 2016);
        secondDay.set(Calendar.DAY_OF_MONTH, 1);


        Calendar thirdDay = Calendar.getInstance();
        thirdDay.set(Calendar.MONTH, Calendar.SEPTEMBER);
        thirdDay.set(Calendar.YEAR, 2016);
        thirdDay.set(Calendar.DAY_OF_MONTH, 2);

        List<Calendar> days = new ArrayList<Calendar>();
        days.add(firstDay);
        days.add(secondDay);
        days.add(thirdDay);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());

        for(int i=0 ; i < days.size() ; i++){
            Calendar day = days.get(i);
            if(day.get(Calendar.YEAR)  == today.get(Calendar.YEAR)
                    && day.get(Calendar.MONTH)  == today.get(Calendar.MONTH)
                    && day.get(Calendar.DAY_OF_MONTH)  == today.get(Calendar.DAY_OF_MONTH)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    public static String[] REPLACES = { "a", "e", "i", "o", "u", "c" };

    public static Pattern[] PATTERNS = null;

    public static void compilePatterns() {
        PATTERNS = new Pattern[REPLACES.length];
        PATTERNS[0] = Pattern.compile("[âãáàä]", Pattern.CASE_INSENSITIVE);
        PATTERNS[1] = Pattern.compile("[éèêë]", Pattern.CASE_INSENSITIVE);
        PATTERNS[2] = Pattern.compile("[íìîï]", Pattern.CASE_INSENSITIVE);
        PATTERNS[3] = Pattern.compile("[óòôõö]", Pattern.CASE_INSENSITIVE);
        PATTERNS[4] = Pattern.compile("[úùûü]", Pattern.CASE_INSENSITIVE);
        PATTERNS[5] = Pattern.compile("[ç]", Pattern.CASE_INSENSITIVE);
    }

    public static String removeAccentuation(String text) {
        if(text != null) {
            if (PATTERNS == null) {
                compilePatterns();
            }

            String result = text;
            for (int i = 0; i < PATTERNS.length; i++) {
                Matcher matcher = PATTERNS[i].matcher(result);
                result = matcher.replaceAll(REPLACES[i]);
            }
            return result.toLowerCase();
        }
        return "";
    }

    public static Date getTime(Integer date, Integer time, boolean isInicio){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.AUGUST);
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, date + 30);

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //calendar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        //calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        if(time < 20) {
            convertTimeToHours(calendar, time, isInicio);
        }
        else{
            Integer hour = 0;

            switch(time) {
                case 20: hour = 9; break;
                case 21: hour = 14; break;
                case 22: hour = 19; break;
                case 23: hour = 9; break;
            }

            hour = isInicio ? hour: hour + 3;

            if(time == 22 && !isInicio){
                hour = 22;
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }

        return calendar.getTime();
    }

    private static void convertTimeToHours(Calendar calendar, Integer time, Boolean isInicio) {

        Integer hour = 0;

        switch(time){
            case 1 : hour = 9; break;
            case 2 : hour = 10; break;
            case 3 : hour = 11; break;
            case 4 : hour = 12; break;
            case 5 : hour = 13; break;
            case 6 : hour = 14; break;

            case 7 : hour = 15; break;
            case 8 : hour = 16; break;
            case 9 : hour = 17; break;
            case 10: hour = 18; break;
            case 11: hour = 19; break;
            case 12: hour = 20; break;
            case 13: hour = 21; break;
        }

        hour = isInicio == Boolean.TRUE ? hour : hour+1;

        calendar.set(Calendar.HOUR_OF_DAY, hour);

        /*// Adicionar 30 minutos
        if(time == 8){
            calendar.set(Calendar.MINUTE, 30);
        }*/

    }

    public static String getPlace(Integer type, Integer place){
        // Oficina
        if(type == 2){
            switch (place) {
                case 9 : return "A301";
                case 10 : return "B106";
                case 11 : return "B107";
                case 12 : return "B108";
                case 13 : return "B109";
                case 14 : return "B201";
                case 15 : return "B202";
                case 16 : return "B301";
            }
        }
        else{
            switch (place) {
                case 1 : return "Auditório";
                case 2 : return "Mini-Auditório";
                case 3 : return "Q-004";
                case 4 : return "Q-101";
                case 5 : return "Q-102";
                case 6 : return "Q-103";
                case 7 : return "Q-105";
                case 8 : return "Q-201";

            }
        }

        return "";
    }
}
