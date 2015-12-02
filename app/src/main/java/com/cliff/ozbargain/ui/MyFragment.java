package com.cliff.ozbargain.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cliff.ozbargain.network.VolleyLoader;

/**
 * Created by Clifford on 24/11/2015.
 */
public class MyFragment extends Fragment {
    private TextView textView;

    public static MyFragment getInstance(int position){
        MyFragment fragment=new MyFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my,container,false);
        textView= (TextView) layout.findViewById(R.id.position);
        Bundle bundle= getArguments();
        if(bundle!=null){
            textView.setText("Page selected is "+ bundle.getInt("position"));
        }
        RequestQueue queue= VolleyLoader.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.ozbargain.com.au/deals/feed", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        return layout;
    }
}