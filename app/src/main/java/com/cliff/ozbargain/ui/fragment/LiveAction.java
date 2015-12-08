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
 * Use the {@link LiveAction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveAction extends Fragment implements DealsLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEALS = "DEALS";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = OZBDeals.class.getSimpleName();
    private RecyclerView rvPopularDeals;
    private DealListAdapter dealListAdapter;
    ArrayList<Deal> deals = new ArrayList<Deal>();
    private SwipeRefreshLayout swipeRefreshLayout = null;

    private ImageView previewImage;


    public LiveAction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveAction.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveAction newInstance(String param1, String param2) {
        LiveAction fragment = new LiveAction();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

//        if (savedInstanceState!=null){
//            deals = (ArrayList<Deal>) savedInstanceState.get(DEALS);
//        }else{
//            OZBApplication.getWritableDatabase().deleteAllDeals();
//            deals=OZBApplication.getWritableDatabase().getAllDeals();
//            if (deals.isEmpty()){
                new DealsTask(this).execute(mParam1,mParam2);
//            }
//        }
//        dealListAdapter.setDeals(deals);

        swipeRefreshLayout.setOnRefreshListener(this);
        return fragmentPopularDeals;
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
        new DealsTask(this).execute(mParam1);
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
