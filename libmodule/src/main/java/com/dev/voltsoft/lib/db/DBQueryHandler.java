package com.dev.voltsoft.lib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IRequestHandler;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.constatns.ValueType;
import com.dev.voltsoft.lib.db.query.*;
import com.dev.voltsoft.lib.model.BaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Observable;

public class DBQueryHandler<R extends DBQuery> implements IRequestHandler<R>
{
    private static class LazyHolder
    {
        private static DBQueryHandler mInstance = new DBQueryHandler();
    }

    public static DBQueryHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    @Override
    public void handle(final R r)
    {
        Context c = r.getContext();

        DBQueryHelper DBQueryHelper = new DBQueryHelper(c);
        DBQueryHelper.open();

        DBQueryType dbQueryType = r.getDbRequestType();

        try
        {
            switch (dbQueryType)
            {
                case QUERY_DELETE:
                {
                    queryDelete(DBQueryHelper, (DBQueryDelete) r);
                    break;
                }

                case QUERY_INSERT:
                {
                    queryInsert(DBQueryHelper, (DBQueryInsert) r);
                    break;
                }

                case QUERY_UPDATE:
                {
                    queryUpdate(DBQueryHelper, (DBQueryUpdate) r);
                    break;
                }

                case QUERY_SELECT:
                {
                    querySelect(DBQueryHelper, (DBQuerySelect) r);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            final IResponseListener responseListener = r.getResponseListener();

            if (responseListener != null && r.getContext() != null)
            {

                new Handler(r.getContext().getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        DBQueryResponse dbQueryResponse = new DBQueryResponse();
                        dbQueryResponse.setResponseCode(-1);
                        dbQueryResponse.setSourceRequest(r);

                        responseListener.onResponseListen(dbQueryResponse);
                    }
                });
            }
        }

        DBQueryHelper.close();
    }

    @SuppressWarnings("unchecked")
    private <M extends BaseModel> void querySelect(DBQueryHelper DBQueryHelper, final DBQuerySelect r) throws Exception
    {
        DBQueryGenerator dbQueryGenerator = r.getDbQueryGenerator();

        String strQuery = dbQueryGenerator.create(r);

        Cursor cursor = DBQueryHelper.query(strQuery);

        if (cursor.moveToFirst())
        {
            DBQueryParcelable<M> dbQueryParcelable = r.getDBQueryParcelable();

            M m = dbQueryParcelable.parse(cursor);

            final DBQueryResponse dbQueryResponse = new DBQueryResponse();
            dbQueryResponse.setResponseCode(1);
            dbQueryResponse.setSourceRequest(r);
            dbQueryResponse.setResponseModel(m);

            final IResponseListener responseListener = r.getResponseListener();

            if (responseListener != null && r.getContext() != null)
            {
                new Handler(r.getContext().getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        responseListener.onResponseListen(dbQueryResponse);
                    }
                });
            }
        }
        else
        {
            throw new Exception();
        }
    }

    private void queryUpdate(DBQueryHelper DBQueryHelper, final DBQueryUpdate r) throws Exception
    {
        boolean bUpdated = DBQueryHelper.updateDBData(r.getTargetInstance());

        if (r.getResponseListener() != null && r.getContext() != null)
        {
            final DBQueryResponse dbQueryResponse = new DBQueryResponse();
            dbQueryResponse.setResponseCode((bUpdated ? 1 : -1));
            dbQueryResponse.setSourceRequest(r);
            dbQueryResponse.setUpdated(bUpdated);

            new Handler(r.getContext().getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    r.getResponseListener().onResponseListen(dbQueryResponse);
                }
            });
        }
    }

    private void queryDelete(DBQueryHelper DBQueryHelper, final DBQueryDelete r) throws Exception
    {
        boolean bDeleted = DBQueryHelper.dropRecord(r.getTargetInstance());

        if (r.getResponseListener() != null && r.getContext() != null)
        {
            final DBQueryResponse dbQueryResponse = new DBQueryResponse();
            dbQueryResponse.setResponseCode((bDeleted ? 1 : -1));
            dbQueryResponse.setSourceRequest(r);
            dbQueryResponse.setInserted(bDeleted);

            new Handler(r.getContext().getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    r.getResponseListener().onResponseListen(dbQueryResponse);
                }
            });
        }
    }

    private void queryInsert(DBQueryHelper DBQueryHelper, final DBQueryInsert r) throws Exception
    {
        boolean bInserted = DBQueryHelper.insertDBDataBulk(r.getTargetInstanceList());

        if (r.getResponseListener() != null && r.getContext() != null)
        {
            final DBQueryResponse dbQueryResponse = new DBQueryResponse();
            dbQueryResponse.setResponseCode((bInserted ? 1 : -1));
            dbQueryResponse.setSourceRequest(r);
            dbQueryResponse.setInserted(bInserted);

            new Handler(r.getContext().getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    r.getResponseListener().onResponseListen(dbQueryResponse);
                }
            });
        }
    }

    @Override
    public void update(Observable observable, Object data)
    {

    }

    private class DBQueryHelper extends SQLiteOpenHelper
    {
        private static final String DB = "cardEngWord.db";

        private static final int    DB_VERSION = 26;

        private SQLiteDatabase mSqLiteDatabase;

        DBQueryHelper(Context context)
        {
            super(context , DB, null , DB_VERSION);
        }

        synchronized public SQLiteDatabase open()
        {
            mSqLiteDatabase = getWritableDatabase();

            return mSqLiteDatabase;
        }

        synchronized public void close()
        {
            if (mSqLiteDatabase != null)
            {
                mSqLiteDatabase.close();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCreate(SQLiteDatabase db)
        {

        }

        @SuppressWarnings("unchecked")
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

        }

        private  <M extends BaseModel> boolean insertDBDataBulk(ArrayList<M> instances)
        {
            boolean result = false;

            for (M t : instances)
            {
                result = insertDBData(t);
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> boolean insertDBData(M instance)
        {
            ContentValues contentValues = new ContentValues();

            for (Field field : instance.fieldList())
            {
                Object dbColumnData = instance.fieldValue(field);

                if (dbColumnData != null)
                {
                    if (dbColumnData instanceof BaseModel)
                    {
                        insertDBData((BaseModel) dbColumnData);

                    }
                    else if (ValueType.INTEGER.isEqualType(field.getType()))
                    {
                        int data = (int) dbColumnData;
                        if (data > 0) {
                            contentValues.put(field.getName(), String.valueOf(data));
                        }

                    }
                    else
                    {
                        contentValues.put(field.getName(), String.valueOf(dbColumnData));
                    }
                }
            }

            if (contentValues.size() > 0)
            {
                String tableName = instance.getClass().getSimpleName();

                return (queryExist(instance) ?
                        updateDBData(tableName, createWhereClause(instance), contentValues) :
                        insertDBData(tableName, contentValues));
            }
            else
            {
                return false;
            }
        }

        private boolean insertDBData(String tableName, ContentValues contentValues)
        {
            try
            {
                return (mSqLiteDatabase.insert(tableName , null , contentValues) != -1);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                close();

                return false;
            }
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> boolean dropRecord(M instance)
        {
            try
            {
                String tableName = instance.getClass().getSimpleName();
                String strWhereClause = createWhereClause(instance);
                int result = getWritableDatabase().delete(tableName , strWhereClause , null);

                return (result > 0);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> boolean updateDBData(M instance)
        {
            try
            {
                String tableName = instance.getClass().getSimpleName();
                String strWhereClause = createWhereClause(instance);
                ContentValues contentValues = createUpdateValues(instance);

                return updateDBData(tableName, strWhereClause , contentValues);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
        }

        private <M extends BaseModel> boolean updateDBData(String tableName , String whereClause , ContentValues contentValues)
        {
            return (mSqLiteDatabase.update(tableName , contentValues , whereClause , null) > 0);
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> boolean queryExist(M instance)
        {
            try
            {
                String tableName = instance.getClass().getSimpleName();

                String whereClause = createWhereClause(instance);

                Cursor cursor = queryWithWhereClause(tableName , whereClause);

                return cursor.moveToFirst();
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
        }

        private Cursor queryWithWhereClause(String tableName , String whereClause)
        {
            if (TextUtils.isEmpty(whereClause))
            {
                whereClause = null;
            }

            return mSqLiteDatabase.query(tableName , null , whereClause , null, null, null, null);
        }

        private Cursor query(String strQuery)
        {
            return mSqLiteDatabase.rawQuery(strQuery , null);
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> String createWhereClause(M instance)
        {
            StringBuilder stringBuilder = new StringBuilder();

            boolean b = false;

            for (Field field : instance.fieldList()) {

                Object dbColumnData = instance.fieldValue(field);

                if (field.isAnnotationPresent(Unique.class) && dbColumnData != null) {

                    if (dbColumnData instanceof Integer) {
                        int data = (int) dbColumnData;
                        if (data == 0) {
                            continue;
                        }
                    }

                    if (b) {
                        stringBuilder.append(" AND ");
                    }
                    stringBuilder.append(field.getName());
                    stringBuilder.append(" = '");
                    stringBuilder.append(String.valueOf(dbColumnData));
                    stringBuilder.append("'");

                    b = true;
                }
            }
            return stringBuilder.toString();
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> ContentValues createUpdateValues(M schemaDecorator)
        {
            ContentValues contentValues = new ContentValues();

            for (Field field : schemaDecorator.fieldList())
            {
                Object dbColumnData = schemaDecorator.fieldValue(field);

                if (!field.isAnnotationPresent(Unique.class) && dbColumnData != null)
                {
                    if (ValueType.INTEGER.isEqualType(field.getType())) {

                        int data = (int) dbColumnData;
                        if (data > 0) {
                            contentValues.put(field.getName(), String.valueOf(data));
                        }
                    } else {

                        contentValues.put(field.getName(), String.valueOf(dbColumnData));

                    }
                }
            }
            return contentValues;
        }

        private <M extends BaseModel> void execCreateQuery(SQLiteDatabase db , Class<? extends M> mClass) {
            try {
                M m = mClass.newInstance();
                execCreateQuery(db , m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private <M extends BaseModel> void execCreateQuery(SQLiteDatabase db , M instance) {
            try {
                String strQuery = queryCreateDBSchema(instance);
                execSQLQuery(db , strQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private <M extends BaseModel> void execUpdateQuery(SQLiteDatabase db , Class<? extends M> mClass) {
            try {
                M m = mClass.newInstance();
                execUpdateQuery(db , m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private <M extends BaseModel> void execUpdateQuery(SQLiteDatabase db , M instance) {
            try {
                String strQuery = queryUpdateDBSchema(instance);
                execSQLQuery(db , strQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void execSQLQuery(SQLiteDatabase db , String query) {
            try {
                if (db != null) {
                    db.execSQL(query);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("unchecked")
        private <M extends BaseModel> String queryCreateDBSchema(M instance)
        {
            boolean b = false;

            StringBuilder primaryKeySet = new StringBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" CREATE TABLE IF NOT EXISTS ");
            stringBuilder.append(instance.getClass().getSimpleName());
            stringBuilder.append(" ( ");
            for (Field field : instance.fieldList()) {
                if (b) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(field.getName());
                stringBuilder.append(" TEXT ");
                if (field.isAnnotationPresent(Unique.class)) {
                    if (primaryKeySet.toString().length() != 0) {
                        primaryKeySet.append(",");
                    }
                    primaryKeySet.append(field.getName());
                }

                b = true;
            }

            if (!TextUtils.isEmpty(primaryKeySet.toString())) {
                stringBuilder.append(",");
                stringBuilder.append("PRIMARY KEY (");
                stringBuilder.append(primaryKeySet.toString());
                stringBuilder.append(")");
            }
            stringBuilder.append(" )");
            return stringBuilder.toString();
        }

        private <M extends BaseModel> String queryUpdateDBSchema(M instance)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf("DROP TABLE IF EXISTS "));
            stringBuilder.append(instance.getClass().getSimpleName());
            return stringBuilder.toString();
        }
    }
}
