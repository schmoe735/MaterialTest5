package com.cliff.ozbargain.services;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.cliff.ozbargain.callbacks.DealsLoadedListener;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.network.VolleyLoader;
import com.cliff.ozbargain.ui.R;
import com.cliff.ozbargain.util.XmlService;

import java.util.ArrayList;

/**
 * Created by Clifford on 1/12/2015.
 */
public class DealsTask extends AsyncTask<String, Void, ArrayList<Deal>> {

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
    protected ArrayList<Deal> doInBackground(String... params) {
        int pages = 5;
        if (params.length>1){
            pages=Integer.parseInt(params[1])>1?1:5;
        }

        return XmlService.getDeals(params[0], requestQueue, 0, pages);

    }


    @Override
    protected void onPostExecute(ArrayList<Deal> deals) {
        dealsLoadedListener.onDealsLoaded(deals);
//        jobFinished(deals, false);
    }
}