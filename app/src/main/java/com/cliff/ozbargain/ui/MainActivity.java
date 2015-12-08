package com.cliff.ozbargain.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cliff.ozbargain.services.DealService;
import com.cliff.ozbargain.ui.fragment.AllDeals;
import com.cliff.ozbargain.ui.fragment.LiveAction;
import com.cliff.ozbargain.ui.fragment.OZBDeals;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class MainActivity extends AppCompatActivity implements MaterialTabListener , View.OnClickListener{

    public static final String HELP = "HELP";
    public static final String INFO = "INFO";
    private static final int JOB_ID = 100;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private MaterialTabHost tabHost;
    private static final int ALL_DEALS = 0;
    private static final int POPULAR_DEALS = 1;
    private static final int CATEGORY = 2;
    private PagerAdapter mPagerAdapter;
    private JobScheduler mJobScheduler;
    private FloatingActionMenu actionMenu;
    private FloatingActionButton actionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment fragment= (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        fragment.setup(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),toolbar);

        tabHost= (MaterialTabHost) findViewById(R.id.materialTabHost);
        mViewPager= (ViewPager) findViewById(R.id.pager);
        mPagerAdapter=new PagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i=0; i<mPagerAdapter.getCount();i++)
        {
            tabHost.addTab(tabHost.newTab()
                    .setText(mPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        //Android Service
        mJobScheduler=JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 300000);
//        constructJob();

        buildFloatingIcon();
    }



    private void buildFloatingIcon() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.vector_icon);
        icon.setPadding(0, 0, 0, 0);

        actionButton = new FloatingActionButton.Builder(this)

                .setContentView(icon)
                .build();

        ImageView helpIcon = new ImageView(this);
        helpIcon.setImageResource(android.R.drawable.ic_menu_help);
        ImageView infoIcon = new ImageView(this);
        infoIcon.setImageResource(android.R.drawable.ic_menu_info_details);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton help = itemBuilder.setContentView(helpIcon).build();
        SubActionButton info =itemBuilder.setContentView(infoIcon).build();
        help.setOnClickListener(this);
        help.setTag(HELP);
        info.setOnClickListener(this);
        info.setTag(INFO);
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(help)
                .addSubActionView(info)
                .attachTo(icon)
                .build();
    }

    private void constructJob(){
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, DealService.class));
//        PersistableBundle persistableBundle = new PersistableBundle();
        builder.setPeriodic(30000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setExtras(persistableBundle)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.navigate){
            startActivity(new Intent(this,SubActivity.class));
            return true;
        }else if(id==R.id.tabslib){
            startActivity(new Intent(this,LibTabsActivity.class));
            return true;
        }else if(id==R.id.vector){
            startActivity(new Intent(this,VectorTestActivity.class));
            return true;
        }  else{
            Toast.makeText(this,"menu clicked"+ id,Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public void onClick(View v) {
        if (HELP.equals(v.getTag())){
//            Fragment fragment= mPagerAdapter.getItem(mViewPager.getCurrentItem());//To access fragment
            startActivity(new Intent(this, SubActivity.class));
        }else if (INFO.equals(v.getTag())){
//            Fragment fragment= mPagerAdapter.getItem(mViewPager.getCurrentItem());

            Fragment fragment= (Fragment) mPagerAdapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());

        }
    }

    public void onDrawerItemClicked(int position) {
        mViewPager.setTag(R.string.live_action,""+position);
        mViewPager.setCurrentItem(position);
    }

    public void onDrawerOpened() {
        mViewPager.setCurrentItem(ALL_DEALS);
    }

    class PagerAdapter extends FragmentStatePagerAdapter{

        int[] icons = new int[]{R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.vector_icon};
        String tabs[];
        String urls[];

//        int[] icons = new int[]{R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.icon_moon,R.drawable.ic_action,R.drawable.ic_facebook};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            tabs=getResources().getStringArray(R.array.tabs);
            urls=getResources().getStringArray(R.array.menu_urls);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case ALL_DEALS:
                    return LiveAction.newInstance(getString(R.string.all_deals_url),""+position);
                case POPULAR_DEALS:
                    return LiveAction.newInstance(getString(R.string.popular_deals_url),""+position);
                case CATEGORY:
                    return getCategoryFragment();

            }
            return MyFragment.getInstance(position);
        }

        private Fragment getCategoryFragment() {
            String category = (String) mViewPager.getTag(R.string.live_action);
            if (category!=null){
                return LiveAction.newInstance(urls[Integer.parseInt(category)],category);
            }
            return LiveAction.newInstance(getString(R.string.popular_deals_url),category);//default
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Drawable drawable=getResources().getDrawable(icons[position]);
//            drawable.setBounds(0,0,60,60);
//            ImageSpan imageSpan=new ImageSpan(drawable);
//            SpannableString spannableString = new SpannableString(" ");
//            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        public Drawable getIcon(int i) {
            return getResources().getDrawable(icons[i]);
        }
    }

    public void onDrawerSlide(float offset){
        if (actionMenu!=null){
            if (actionMenu.isOpen()){
                actionMenu.close(true);
            }
            actionButton.setTranslationX(offset*300);
        }
    }


}
