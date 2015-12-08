package com.cliff.ozbargain.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.cliff.ozbargain.model.Deal;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Clifford on 30/11/2015.
 */
public class XmlService {

    private static final String TAG=XmlService.class.getSimpleName();


    public static ArrayList<Deal> getDeals(String url, final RequestQueue requestQueue, int startPage, int endPage){
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
                HttpsURLConnection connection = getFeed(url + pageParam);
                try{

                    deals.addAll(DomXmlMapper.xmlToDeal(connection.getInputStream()));
                }catch (Exception e){

                }finally {
                    connection.disconnect();
                    Thread.sleep(100);
                }
//                deals.addAll(DomXmlMapper.xmlToDeal(xml));

            } catch (Exception e) {
                L.d(TAG, "Error while retreiving feed",e);
            }

        }
        return deals;
    }

    private static HttpsURLConnection getFeed(String downloadUrl) {
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(downloadUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDefaultUseCaches(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36");
            urlConnection.setRequestProperty("Upgrade-Insecure-Requests","1");
            urlConnection.setRequestProperty("Connection","keep-alive");
            urlConnection.setRequestProperty("Cache-Control","max-age=0");
            urlConnection.setRequestProperty("Accept-Language","en-US,en;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-AU;q=0.2");
            urlConnection.setRequestProperty("Accept-Encoding","gzip, deflate, sdch");
            urlConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            long currentTime = System.currentTimeMillis();
            long expires = urlConnection.getHeaderFieldDate("Expires", currentTime);
            long lastModified = urlConnection.getHeaderFieldDate("Last-Modified", currentTime);



            urlConnection.setInstanceFollowRedirects(true);
//            InputStream inputStream = urlConnection.getInputStream();
            return urlConnection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            urlConnection.disconnect();
        }
        return null;

    }



}
