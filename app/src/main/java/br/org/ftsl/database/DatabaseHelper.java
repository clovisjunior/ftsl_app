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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.org.ftsl.model.AuthorModel;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Constant;

/**
 * Created by 05081364908 on 16/07/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "ftsl.db";
    private static final int DATABASE_VERSION = 1;

    private static String DATABASE_PATH = Environment.getDataDirectory() + "/data/";

    private RuntimeExceptionDao<ItemGridModel, Integer> mItemGridRuntimeDao = null;
    private RuntimeExceptionDao<AuthorModel, Integer> mAuthorRuntimeDao = null;
    private Context mContext;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        this.mContext = context;

        /*boolean dbExist = checkDataBase();

        if (!dbExist) {
            try {

                getReadableDatabase();
                close();

                InputStream input = context.getAssets().open(DATABASE_NAME);
                String outFileName = DATABASE_PATH + context.getApplicationContext().getPackageName() + "/databases/" + DATABASE_NAME;

                Log.i(Constant.TAG, "DB Path : " + outFileName);

                OutputStream output = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();
            }
            catch (IOException e) {
                Log.e(Constant.TAG, "Can't copy database", e);
            }
        }*/
    }

    /*private boolean checkDataBase() {
        boolean checkDB;

        String path = DATABASE_PATH + mContext.getApplicationContext().getPackageName() + "/databases/" + DATABASE_NAME;;
        File dbFile = new File(path);
        checkDB = dbFile.exists();

        Log.i(Constant.TAG, "DB Exist : " + checkDB);

        return checkDB;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(Constant.TAG, "onCreate");
            //if(!checkDataBase()) {
                TableUtils.createTable(connectionSource, ItemGridModel.class);
                TableUtils.createTable(connectionSource, AuthorModel.class);
            //}
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

            onCreate(db, connectionSource);
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<ItemGridModel, Integer> getItemGridDao() {
        if (mItemGridRuntimeDao == null) {
            mItemGridRuntimeDao = getRuntimeExceptionDao(ItemGridModel.class);
        }
        return mItemGridRuntimeDao;
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
    }

    public void removeAllItemGrid(){

        List<ItemGridModel> items = getItemGridDao().queryForAll();
        getItemGridDao().delete(items);

        List<AuthorModel> itemGridAuthors = getAuthorDao().queryForAll();
        getAuthorDao().delete(itemGridAuthors);

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

    public List<ItemGridModel> getItemsGrid(Integer date, Integer sessionType, Boolean isAgenda) {

        try {
            QueryBuilder<ItemGridModel, Integer> queryBuilder = getItemGridDao().queryBuilder();
            Where<ItemGridModel, Integer> where = queryBuilder.where();

            where.eq("date", date);

            if (sessionType > 0) {
                where.and().eq("type", sessionType);
            }

            if(isAgenda){
                where.and().eq("assistir", Boolean.TRUE);
            }

            queryBuilder.orderBy("inicio", true);
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

            where.eq("assistir", Boolean.TRUE);
            where.and().ge("inicio", new Date(System.currentTimeMillis()));

            queryBuilder.orderBy("inicio", true);
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
