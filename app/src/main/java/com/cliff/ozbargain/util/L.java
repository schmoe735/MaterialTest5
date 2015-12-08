package com.cliff.ozbargain.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ocpsoft.pretty.time.PrettyTime;

import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Clifford on 27/11/2015.
 */
public class L {
    public static final String RESULT = "RESULT";
    public static final String ERROR = "ERROR";
    public static final String PAGE_PARAM ="?page=%d";
    public static final String DEAL_TYPE= "DEAL_TYPE";
    private static final PrettyTime prettyTime = new PrettyTime();

    public static void m(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void d(String tag, String text){
        d(tag, text, null);
    }
    public static void d(String tag, String text, Exception e){
        Log.d(tag, text, e);
    }

    public static String time(Date date){
        return  prettyTime.format(date);
    }
}
