package com.cliff.ozbargain.core;

import android.app.Application;
import android.content.Context;

import com.cliff.ozbargain.db.DealsDatabase;
import com.cliff.ozbargain.ui.R;

import java.net.ConnectException;
import java.util.HashMap;

/**
 * Created by Clifford on 25/11/2015.
 */
public class OZBApplication extends Application {
    private static OZBApplication application;
    private static DealsDatabase dealsDatabase;

    public synchronized static DealsDatabase getWritableDatabase(){
        if (dealsDatabase==null){
            dealsDatabase= new DealsDatabase(getAppContext());

            dealsDatabase.insertMeta(getMetaAsMap());
        }
        return dealsDatabase;
    }

    private static HashMap<String, String> getMetaAsMap() {
        String[]  metaArray = getAppContext().getResources().getStringArray(R.array.meta);
        HashMap<String,String> metaMap = new HashMap<>(metaArray.length);
        for (String meta : metaArray) {
            String[] metaval = meta.split("|");
            metaMap.put(metaval[0], metaval[1]);
        }

        return metaMap;
    }

    public void onCreate(){
        super.onCreate();
        application=this;
    }

    public static OZBApplication getInstance(){
        return application;
    }

    public static Context getAppContext(){
        return application.getApplicationContext();
    }
}
