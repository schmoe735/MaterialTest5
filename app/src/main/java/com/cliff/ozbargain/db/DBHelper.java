package com.cliff.ozbargain.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.cliff.ozbargain.util.L;

/**
 * Created by Clifford on 1/12/2015.
 */
public abstract class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String OPEN_BRACKET = " ( ";

    private static final String CLOSE_BRACKET = " ) ";
    protected static final String COLUMN_UID = "ID";
    private static final String PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    private static final String COMMA =" , ";





    private static final String SPACE = " ";
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    protected String generateCreateTableScript(String tableName, String... columnNames){
        StringBuffer tableScript = new StringBuffer();
        tableScript.append(CREATE_TABLE);
        tableScript.append(tableName);
        tableScript.append(OPEN_BRACKET);
        tableScript.append(COLUMN_UID);
        tableScript.append(PRIMARY_KEY_TYPE);
        tableScript.append(COMMA);
        for (String columnName :
                columnNames) {
            tableScript.append(columnName);
            tableScript.append(getTypeInfo(columnName));
            tableScript.append(COMMA);
        }
        tableScript.deleteCharAt(tableScript.length()-2);
        tableScript.append(CLOSE_BRACKET);
        L.d(TAG,tableScript.toString());
        return tableScript.toString();
    }

    private String getTypeInfo(String columnName) {
        if (columnName.startsWith("I")){
            return " INTEGER ";
        }else if (columnName.startsWith("D")){
            return " DATE ";
        }else {
            return " TEXT ";
        }
    }



    protected String generateDropTableScript(String dealTableName) {
        L.d(TAG,"Creating table "+dealTableName);
        return "DROP TABLE  IF EXISTS '"+ dealTableName + "'";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            for (String tableName :
                    getTableNames()) {
                db.execSQL(generateDropTableScript(tableName));
            }

            onCreate(db);
        }catch (SQLiteException e){
            L.d(getTag(), " Error while upgrading the database", e);
        }
    }

    protected abstract String getTag() ;
    protected abstract String[] getTableNames();
}
