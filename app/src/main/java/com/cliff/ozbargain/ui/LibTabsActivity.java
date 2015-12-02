package com.cliff.ozbargain.ui;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.View;

import com.cliff.ozbargain.ui.R;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class LibTabsActivity extends AppCompatActivity implements MaterialTabListener{

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_tabs);
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabHost= (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager= (ViewPager) findViewById(R.id.libpager);

        PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        for (int i=0; i<adapter.getCount();i++)
        {
            tabHost.addTab(tabHost.newTab()
//                    .setText(adapter.getPageTitle(i))
                    .setIcon(adapter.getIcon(i))
                    .setTabListener(this));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu., menu);
        return false;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


    class PagerAdapter extends FragmentPagerAdapter {

        String tabs[];
        int[] icons = new int[]{R.drawable.vector_icon, R.drawable.vector_icon, R.drawable.vector_icon};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            tabs=getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment=MyFragment.getInstance(position);
            return myFragment;
        }



        @Override
        public CharSequence getPageTitle(int position) {
//            Drawable drawable=getResources().getDrawable(icons[position], getTheme());
//            drawable.setBounds(0,0,36,36);
//            ImageSpan imageSpan=new ImageSpan(drawable);
//            SpannableString spannableString = new SpannableString(" ");
//            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            return spannableString;
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        private Drawable getIcon(int position){
            return getResources().getDrawable(icons[position]);
        }
    }
}
