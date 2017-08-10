package br.org.ftsl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.org.ftsl.model.AboutModel;
import br.org.ftsl.model.AuthorModel;
import br.org.ftsl.model.ClassRoomModel;
import br.org.ftsl.model.DayModel;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.model.TimeModel;
import br.org.ftsl.model.TypeModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;
import br.org.ftsl.utils.Utils;

/**
 * Created by 05081364908 on 16/07/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "ftsl.db";
    private static final int DATABASE_VERSION = 7;

    private RuntimeExceptionDao<ItemGridModel, Integer> mItemGridRuntimeDao = null;
    private RuntimeExceptionDao<AuthorModel, Integer> mAuthorRuntimeDao = null;
    private RuntimeExceptionDao<ClassRoomModel, Integer> mClassRoomRuntimeDao = null;
    private RuntimeExceptionDao<TimeModel, Integer> mTimeRuntimeDao = null;
    private RuntimeExceptionDao<TypeModel, Integer> mTypeRuntimeDao = null;
    private RuntimeExceptionDao<DayModel, Integer> mDayRuntimeDao = null;
    private RuntimeExceptionDao<AboutModel, Integer> mAboutRuntimeDao = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(Constant.TAG, "onCreate");
            TableUtils.createTable(connectionSource, ItemGridModel.class);
            TableUtils.createTable(connectionSource, AuthorModel.class);
            TableUtils.createTable(connectionSource, ClassRoomModel.class);
            TableUtils.createTable(connectionSource, TypeModel.class);
            TableUtils.createTable(connectionSource, DayModel.class);
            TableUtils.createTable(connectionSource, TimeModel.class);
            TableUtils.createTable(connectionSource, AboutModel.class);
        }
        catch (SQLException e) {
            Log.e(Constant.TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(Constant.TAG, "onUpgrade");

            TableUtils.dropTable(connectionSource, ItemGridModel.class, true);
            TableUtils.dropTable(connectionSource, AuthorModel.class, true);
            TableUtils.dropTable(connectionSource, ClassRoomModel.class, true);
            TableUtils.dropTable(connectionSource, TypeModel.class, true);
            TableUtils.dropTable(connectionSource, DayModel.class, true);
            TableUtils.dropTable(connectionSource, TimeModel.class, true);
            TableUtils.dropTable(connectionSource, AboutModel.class, true);

            onCreate(db, connectionSource);
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<ClassRoomModel, Integer> getClassRoomDao() {
        if (mClassRoomRuntimeDao == null) {
            mClassRoomRuntimeDao = getRuntimeExceptionDao(ClassRoomModel.class);
        }
        return mClassRoomRuntimeDao;
    }

    public RuntimeExceptionDao<ItemGridModel, Integer> getItemGridDao() {
        if (mItemGridRuntimeDao == null) {
            mItemGridRuntimeDao = getRuntimeExceptionDao(ItemGridModel.class);
        }
        return mItemGridRuntimeDao;
    }

    public RuntimeExceptionDao<DayModel, Integer> getDayDao() {
        if (mDayRuntimeDao == null) {
            mDayRuntimeDao = getRuntimeExceptionDao(DayModel.class);
        }
        return mDayRuntimeDao;
    }

    public RuntimeExceptionDao<TimeModel, Integer> getTimeDao() {
        if (mTimeRuntimeDao == null) {
            mTimeRuntimeDao = getRuntimeExceptionDao(TimeModel.class);
        }
        return mTimeRuntimeDao;
    }

    public RuntimeExceptionDao<TypeModel, Integer> getTypeDao() {
        if (mTypeRuntimeDao == null) {
            mTypeRuntimeDao = getRuntimeExceptionDao(TypeModel.class);
        }
        return mTypeRuntimeDao;
    }

    public RuntimeExceptionDao<AboutModel, Integer> getAboutDao() {
        if (mAboutRuntimeDao == null) {
            mAboutRuntimeDao = getRuntimeExceptionDao(AboutModel.class);
        }
        return mAboutRuntimeDao;
    }

    public RuntimeExceptionDao<AuthorModel, Integer> getAuthorDao() {
        if (mAuthorRuntimeDao == null) {
            try {
                mAuthorRuntimeDao = getRuntimeExceptionDao(AuthorModel.class);
            }
            catch(Exception e){}
        }
        return mAuthorRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        mItemGridRuntimeDao = null;
        mAuthorRuntimeDao = null;
        mClassRoomRuntimeDao = null;
        mDayRuntimeDao = null;
        mTimeRuntimeDao = null;
        mTypeRuntimeDao = null;
        mAboutRuntimeDao = null;
    }

    public void removeAllItemGrid(){

        List<ItemGridModel> items = getItemGridDao().queryForAll();
        getItemGridDao().delete(items);

        List<AuthorModel> itemGridAuthors = getAuthorDao().queryForAll();
        getAuthorDao().delete(itemGridAuthors);

    }

    public void removeAllClassRoom() {
        List<ClassRoomModel> classes = getClassRoomDao().queryForAll();
        getClassRoomDao().delete(classes);
    }

    public void removeAllTypes() {
        List<TypeModel> types = getTypeDao().queryForAll();
        getTypeDao().delete(types);
    }

    public void removeAllDays() {
        List<DayModel> days = getDayDao().queryForAll();
        getDayDao().delete(days);
    }

    public void removeAllTimes() {
        List<TimeModel> times = getTimeDao().queryForAll();
        getTimeDao().delete(times);
    }

    public void createItemGrid(ItemGridModel item) {
        getItemGridDao().create(item);
        if(getAuthorDao().queryForId(item.getAuthor().getId()) == null) {
            getAuthorDao().create(item.getAuthor());
        }
        else{
            getAuthorDao().update(item.getAuthor());
        }
    }

    public void removeAbout() {
        List<AboutModel> about = getAboutDao().queryForAll();
        getAboutDao().delete(about);
    }

    public void createClassRoom(ClassRoomModel classRoom) {
        getClassRoomDao().create(classRoom);
    }

    public void createAbout(AboutModel about) {
        getAboutDao().create(about);
    }

    public void createDay(DayModel day) {
        getDayDao().create(day);
    }

    public void createTime(TimeModel time) {
        getTimeDao().create(time);
    }

    public void createType(TypeModel type) {
        getTypeDao().create(type);
    }

    public void updateItemGrid(TimeModel time) {
        try {
            QueryBuilder<ItemGridModel, Integer> queryBuilder = getItemGridDao().queryBuilder();
            Where<ItemGridModel, Integer> where = queryBuilder.where();

            where.eq("time", time.getId());

            List<ItemGridModel> results = where.query();

            if(results != null) {

                Map<Integer, Date> dayMap = new HashMap<>();
                for (DayModel day : getDayDao().queryForAll()) {
                    dayMap.put(day.getId(), day.getDay());
                }

                for(ItemGridModel itemGrid : results) {
                    Integer timeId = itemGrid.getTime();
                    Integer dayId = itemGrid.getDate();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dayMap.get(dayId));

                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    itemGrid.setStart(Utils.getTime(calendar, timeId, true));
                    itemGrid.setEnd(Utils.getTime(calendar, timeId, false));

                    getItemGridDao().update(itemGrid);
                }

            }
        }
        catch(SQLException e){
            Log.e(Constant.TAG, "Error on update time of itemgrid", e);
        }
    }

    public String getPlaceDescription(ItemGridModel itemGrid) {

        try {
            QueryBuilder<ClassRoomModel, Integer> queryBuilder = getClassRoomDao().queryBuilder();
            Where<ClassRoomModel, Integer> where = queryBuilder.where();

            where.eq("type", itemGrid.getType());
            where.and().eq("place", itemGrid.getPlace());

            ClassRoomModel result = where.queryForFirst();

            if(result != null) {
                return result.getDescription();
            }
        }
        catch(SQLException e){
            Log.e(Constant.TAG, "Error on fetch place description", e);
        }

        return "";
    }

    public List<ItemGridModel> getItemsGrid(Integer date, Integer sessionType, Boolean isAgenda) {

        try {
            QueryBuilder<ItemGridModel, Integer> queryBuilder = getItemGridDao().queryBuilder();
            Where<ItemGridModel, Integer> where = queryBuilder.where();

            where.eq("date", date);

            if (sessionType > 0) {
                where.and().eq("type", sessionType);
            }

            if(isAgenda){
                where.and().eq("isWatch", Boolean.TRUE);
            }

            queryBuilder.orderBy("start", true);
            queryBuilder.orderBy("title", true);

            List<ItemGridModel> items = where.query();

            return items;
        }
        catch(SQLException e){
            Log.e(Constant.TAG, "Error on fetch itens", e);
        }

        return null;
    }

    public List<ItemGridModel> getAgenda(){

        try {
            QueryBuilder<ItemGridModel, Integer> queryBuilder = getItemGridDao().queryBuilder();
            Where<ItemGridModel, Integer> where = queryBuilder.where();

            where.eq("isWatch", Boolean.TRUE);
            where.and().ge("start", new Date(System.currentTimeMillis()));

            queryBuilder.orderBy("start", true);
            queryBuilder.orderBy("title", true);

            List<ItemGridModel> items = where.query();

            return items;
        }
        catch(SQLException e){
            Log.e(Constant.TAG, "Error on fetch itens", e);
        }

        return null;
    }

    public List<ItemGridModel> getItemGridByProposta(Integer pid){

        try {
            QueryBuilder<ItemGridModel, Integer> queryBuilder = getItemGridDao().queryBuilder();
            Where<ItemGridModel, Integer> where = queryBuilder.where();

            where.eq("pid", pid);
            List<ItemGridModel> items = where.query();

            return items;
        }
        catch(SQLException e){
            Log.e(Constant.TAG, "Error on fetch item by pid: " + pid, e);
        }

        return null;
    }


}
