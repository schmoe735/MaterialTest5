package com.cliff.ozbargain.ui;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cliff.ozbargain.model.MenuItem;
import com.cliff.ozbargain.tools.MenuAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String PREF_FILE_NAME = "nav_pref";
    private static final String KEY_USER_LEARNED_DRAWER = "USER_LEARNED_DRAWER";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mContainerView;
    private MenuAdapter menuAdapter;

    //User is aware of the drawers existence or not
    private boolean mUserLearnedDrawer;

    //fragment is being started for the very first time or after a rotation
    private boolean mFromSavedInstanceState;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //check if app is already started
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readPreference(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState != null){//created for the first time
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerListRecycler);
        menuAdapter = new MenuAdapter(getActivity(),getMenus());
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainActivity)getActivity()).onDrawerItemClicked(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(),"onLongClick"+position,Toast.LENGTH_SHORT).show();
            }
        }));

        return layout;
    }


    public void setup(int fragment_nav_drawer, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mDrawerLayout=drawerLayout;
        mContainerView=getActivity().findViewById(fragment_nav_drawer);
        mDrawerToggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                //check if its the first time. save to shared pref and check
                if (!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"true");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(slideOffset<0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(mContainerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context,String preference, String value){
        SharedPreferences sharedPreferences= context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preference, value);
        editor.apply();//asynh and faster than commit which is sync

    }

    public static String readPreference(Context context, String prefernce, String defaultVal){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefernce,defaultVal);
    }

    private List<MenuItem> getMenus(){
        List<MenuItem> menusList = new ArrayList<MenuItem>();
        String[] menus = getResources().getStringArray(R.array.categories);
        int[] menuIcons = new int[]{R.drawable.icon_p,R.drawable.icon_a,R.drawable.icon_n,R.drawable.icon_f,R.drawable.icon_h};
        for (int i=0; i<menus.length;i++){
            menusList.add(new MenuItem(menus[i],menuIcons[i%menuIcons.length]));
        }
        return menusList;
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
