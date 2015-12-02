package com.cliff.ozbargain.db;

import static com.cliff.ozbargain.db.DealsHelper.*;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.util.L;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Clifford on 1/12/2015.
 */
public class DealsDatabase {
    private static final String TAG = DealsDatabase.class.getSimpleName();
    private DealsHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DealsDatabase(Context context) {
        mHelper = new DealsHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertDeals(ArrayList<Deal> dealList, boolean clearPrevious){
        if (clearPrevious){
            deleteAll();
        }

        String sql = "INSERT INTO " + DealsHelper.DEAL_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (Deal deal : dealList) {
            statement.clearBindings();
            statement.bindString(2, deal.getTitle());
            statement.bindLong(3, deal.getDateObj().getTime());
            statement.bindLong(4, deal.getPosRating());
            statement.bindLong(5, deal.getNegRating());
            statement.bindString(6, deal.getDescription());
            statement.bindString(7, deal.getImageUri());
            statement.bindString(8, deal.getCreator());
            statement.bindString(9, deal.getOzDealLink());
            statement.bindString(10, deal.getExtDealUrl());
            statement.bindLong(11, deal.getCommentCount());
            statement.bindLong(12, deal.getClickCount());
            L.d(TAG, "Inserting record" + deal);
            statement.execute();

        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Deal> getAllDeals(){
        ArrayList<Deal> deals = new ArrayList<>();
        String[] columns = {COLUMN_UID,COLUMN_TITLE, COLUMN_DATE, COLUMN_POSRATING,COLUMN_NEGRATING,COLUMN_DESC,COLUMN_IMG_URI,COLUMN_IMG_URI,
                COLUMN_CREATOR_ID, COLUMN_OZB_DEAL_LINK, COLUMN_EXT_DEAL_URL,COLUMN_COMMENT_COUNT,COLUMN_CLICK_COUNT};
        Cursor cursor = mDatabase.query(DEAL_TABLE_NAME, columns, null,null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            do {
                Deal deal = new Deal();
                deal.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                deal.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
                deal.setPosRating((int) cursor.getLong(cursor.getColumnIndex(COLUMN_POSRATING)));
                deal.setNegRating((int) cursor.getLong(cursor.getColumnIndex(COLUMN_NEGRATING)));
                deal.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
                deal.setImageUri(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_URI)));
                deal.setCreator(cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR_ID)));
                deal.setOzDealLink(cursor.getString(cursor.getColumnIndex(COLUMN_OZB_DEAL_LINK)));
                deal.setExtDealUrl(cursor.getString(cursor.getColumnIndex(COLUMN_EXT_DEAL_URL)));
                deal.setCommentCount((int) cursor.getLong(cursor.getColumnIndex(COLUMN_COMMENT_COUNT)));
                deal.setClickCount((int)cursor.getLong(cursor.getColumnIndex(COLUMN_CLICK_COUNT)));
                L.d(TAG, "deal retrieved"+ deal);
                deals.add(deal);

            }while (cursor.moveToNext());
        }
        return deals;
    }

    private void deleteAll() {
        mDatabase.delete(DEAL_TABLE_NAME, null, null);
    }
}
