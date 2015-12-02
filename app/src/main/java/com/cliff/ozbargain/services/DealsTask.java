package com.cliff.ozbargain.services;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.cliff.ozbargain.callbacks.DealsLoadedListener;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.network.VolleyLoader;
import com.cliff.ozbargain.util.XmlService;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;

/**
 * Created by Clifford on 1/12/2015.
 */
public class DealsTask extends AsyncTask<Void, Void, ArrayList<Deal>> {

    private VolleyLoader volleyLoader;
    private RequestQueue requestQueue;

    DealsLoadedListener dealsLoadedListener;

    public DealsTask(DealsLoadedListener dealsLoadedListener) {
        this.dealsLoadedListener = dealsLoadedListener;
        volleyLoader=VolleyLoader.getInstance();
        requestQueue=volleyLoader.getRequestQueue();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected ArrayList<Deal> doInBackground(Void... params) {

        return XmlService.getFeed("https://www.ozbargain.com.au/deals/feed", requestQueue, 0, 5);

    }


    @Override
    protected void onPostExecute(ArrayList<Deal> deals) {
        dealsLoadedListener.onDealsLoaded(deals);
//        jobFinished(deals, false);
    }
}