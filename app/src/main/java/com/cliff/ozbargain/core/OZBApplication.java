package com.cliff.ozbargain.core;

import android.app.Application;
import android.content.Context;

import com.cliff.ozbargain.db.DealsDatabase;

import java.net.ConnectException;

/**
 * Created by Clifford on 25/11/2015.
 */
public class OZBApplication extends Application {
    private static OZBApplication application;
    private static DealsDatabase dealsDatabase;

    public synchronized static DealsDatabase getWritableDatabase(){
        if (dealsDatabase==null){
            dealsDatabase= new DealsDatabase(getAppContext());
        }
        return dealsDatabase;
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
