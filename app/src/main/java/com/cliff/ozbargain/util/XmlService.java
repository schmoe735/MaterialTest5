package com.cliff.ozbargain.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.network.VolleyLoader;
import com.cliff.ozbargain.tools.DealListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Clifford on 30/11/2015.
 */
public class XmlService {

    private static final String TAG=XmlService.class.getSimpleName();


    public static ArrayList<Deal> getFeed(String url, final RequestQueue requestQueue, int startPage, int endPage){
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        ArrayList<Deal> deals = new ArrayList<>((endPage-startPage)*10);

        String pageParam = null;
        for (int i=startPage;i<endPage;i++) {
            String xml = null;
            pageParam=String.format(L.PAGE_PARAM,i);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url+ pageParam, requestFuture, requestFuture);

            try {

                requestQueue.add(stringRequest);
                xml = requestFuture.get(30000, TimeUnit.MILLISECONDS);
                deals.addAll(DomXmlMapper.xmlToDeal(xml));

            } catch (Exception e) {
                L.d(TAG, "Error while retreiving feed",e);
            }
            OZBApplication.getWritableDatabase().insertDeals(deals, true);
        }
        return deals;
    }


}
