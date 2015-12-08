package com.cliff.ozbargain.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cliff.ozbargain.animation.AnimatonUtils;
import com.cliff.ozbargain.callbacks.DealsLoadedListener;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.services.DealsTask;
import com.cliff.ozbargain.tools.DealListAdapter;
import com.cliff.ozbargain.ui.DividerItemDecoration;
import com.cliff.ozbargain.ui.R;
import com.cliff.ozbargain.util.L;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OZBDeals#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OZBDeals extends Fragment implements DealsLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String DEALS = "DEALS";

    private static final String TAG = OZBDeals.class.getSimpleName();
    private RecyclerView rvPopularDeals;

    private DealListAdapter dealListAdapter;
    ArrayList<Deal> deals = new ArrayList<Deal>();
    private SwipeRefreshLayout swipeRefreshLayout = null;

    private ImageView previewImage;

    public OZBDeals() {
    }


    public static OZBDeals newInstance(String dealType, String param2) {
        OZBDeals fragment = new OZBDeals();
        Bundle args = new Bundle();
        args.putString(L.DEAL_TYPE, dealType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View fragmentPopularDeals = inflater.inflate(R.layout.popular_deals, container, false);
        rvPopularDeals = (RecyclerView) fragmentPopularDeals.findViewById(R.id.rvPopularDeals);
        previewImage = (ImageView) fragmentPopularDeals.findViewById(R.id.imagePreview);

        rvPopularDeals.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout = (SwipeRefreshLayout) fragmentPopularDeals.findViewById(R.id.swipeToRefreshDeals);
        dealListAdapter=new DealListAdapter(getActivity());
        rvPopularDeals.setAdapter(dealListAdapter);
        rvPopularDeals.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rvPopularDeals, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                previewImage.setVisibility(View.GONE);
                View description = view.findViewById(R.id.description);


                AnimatonUtils.animate(description);
                if (View.VISIBLE == description.getVisibility()) {
                    view.setBackgroundColor(getResources().getColor(android.R.color.white));

                    description.setVisibility(View.GONE);
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.accent));
                    description.setBackgroundColor(getResources().getColor(android.R.color.white));
                    description.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(getActivity(), "onLongClick" + position, Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(dealListAdapter.getDeals().get(position).getOzDealLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }));
        rvPopularDeals.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        if (savedInstanceState!=null){
            deals = (ArrayList<Deal>) savedInstanceState.get(DEALS);
        }else{
            OZBApplication.getWritableDatabase().deleteAllDeals();
            deals=OZBApplication.getWritableDatabase().getAllDeals();
            if (deals.isEmpty()){
                new DealsTask(this).execute(getResources().getString(R.string.all_deals_url));
            }
        }
        dealListAdapter.setDeals(deals);

        swipeRefreshLayout.setOnRefreshListener(this);
        return fragmentPopularDeals;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DEALS, dealListAdapter.getDeals());

    }

    @Override
    public void onDealsLoaded(ArrayList<Deal> deals) {
        if (swipeRefreshLayout.isRefreshing()){
            dealListAdapter.getDeals().clear();
            swipeRefreshLayout.setRefreshing(false);
        }
        dealListAdapter.setDeals(deals);
        dealListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new DealsTask(this).execute(getResources().getString(R.string.all_deals_url));
    }

    class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{

        GestureDetector gestureDetector=null;
        ClickListener listener =null;

        public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView, final ClickListener listener) {
            this.listener=listener;
            gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child!=null && listener!=null){
                        listener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && listener != null && gestureDetector.onTouchEvent(e)){
                listener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view, int position);
    }
}
