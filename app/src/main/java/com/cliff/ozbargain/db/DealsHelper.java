package com.cliff.ozbargain.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cliff.ozbargain.util.L;

/**
 * Created by Clifford on 1/12/2015.
 */
public class DealsHelper extends   DBHelper {
    //DB Details
    protected static final String DB_NAME = "ozb_deals_db";
    private static final int DB_VERSION = 9;
    private static final String TAG = DealsHelper.class.getSimpleName();
    private Context mContext;

    //Deal Table
    protected static final String DEAL_TABLE_NAME = "DEAL";
    protected static final String COLUMN_TITLE = "T_TITLE";
    protected static final String COLUMN_DATE = "D_PUBDATE";
    protected static final String COLUMN_POSRATING = "I_POS";
    protected static final String COLUMN_NEGRATING = "I_NEG";
    protected static final String COLUMN_DESC = "T_DESC";
    protected static final String COLUMN_IMG_URI = "T_IMG_URI";
    protected static final String COLUMN_CREATOR_ID ="T_CREATOR_ID";
    protected static final String COLUMN_OZB_DEAL_LINK = "T_OZB_DEAL_LINK";
//    private List<String> categories;
//    private List<String> comments;
    protected static final String COLUMN_EXT_DEAL_URL = "T_EXT_DEAL_URL";
    protected static final String COLUMN_COMMENT_COUNT = "I_COMMENT_COUNT";
    protected static final String COLUMN_CLICK_COUNT = "I_CLICK_COUNT";

    //Category Table
    protected static final String CATEGORY_TABLE_NAME="CATEGORY";
    protected static final String CATEGORY_ID = "I_CATEGORY_ID";
    protected static final String CATEGORY_NAME = "T_CATEGORY_NAME";

    //Comments table
    protected static final String COMMENTS_TABLE_NAME="COMMENTS";
    protected static final String COMMENT_ID = "I_COMMENT_ID";
    protected static final String COMMENT_DATE = "D_COMMENT_DATE";
    protected static final String COMMENT = "T_COMMENT";

    public DealsHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(generateCreateTableScript(DEAL_TABLE_NAME, COLUMN_TITLE, COLUMN_DATE, COLUMN_POSRATING,
                    COLUMN_NEGRATING, COLUMN_DESC, COLUMN_IMG_URI, COLUMN_CREATOR_ID, COLUMN_OZB_DEAL_LINK, COLUMN_EXT_DEAL_URL, COLUMN_COMMENT_COUNT, COLUMN_CLICK_COUNT));
            L.d(TAG, " Deals Table created ");
        }catch (SQLiteException e){
            L.d(TAG, " Exception while creating the database", e);
        }
    }


    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected String[] getTableNames() {
        return new String[]{DEAL_TABLE_NAME};
    }
}
