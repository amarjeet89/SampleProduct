package com.prafull.product.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.graphics.drawable.ColorDrawable;
import com.prafull.product.R;
import com.prafull.product.adapter.NavigationDrawerAdapter;
import com.prafull.product.fragments.EditProfileFragment;
import com.prafull.product.fragments.ProductFragment;
import com.prafull.product.pojo.NavigationMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubhansu on 2/8/2015.
 */

public class NavigationDrawerActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ListView listview;
    ArrayList<NavigationMenu> nav_titles;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    LinearLayout leftView;
    LayoutInflater layoutInflater;
    Fragment fragment = null;
    String[] titles;
    TypedArray navMenuIcons;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(android.os.Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_navigation);
        initializeControls();
        if(savedInstanceState == null){
            displayGenericFragment();
        }
    }

    private void initializeControls() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.nav_decor, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) mDrawerLayout.findViewById(R.id.drawer_content); // This is the container we defined just now.
        container.addView(child, 0);
        mDrawerLayout.findViewById(R.id.left_drawer_linear).setPadding(0, getStatusBarHeight(), 0, 0);

        // Make the drawer replace the first child
        decor.addView(mDrawerLayout);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));

      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        listview = (ListView) mDrawerLayout.findViewById(R.id.left_drawer);
        leftView = (LinearLayout) mDrawerLayout.findViewById(R.id.left_drawer_linear);
        mTitle = mDrawerTitle = getTitle();

        titles =NavigationDrawerActivity.this.getResources().getStringArray(
                R.array.navigation_menu);

        nav_titles = new ArrayList<NavigationMenu>();
        Log.i(NavigationDrawerActivity.class.getSimpleName(), ""+titles.length);

        for (int i = 0; i < titles.length; i++) {
            nav_titles.add(new NavigationMenu(titles[i], -1));
        }
        listview.setAdapter(new NavigationDrawerAdapter(NavigationDrawerActivity.this, nav_titles));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fragment = new ProductFragment();
                        break;
                    case 1:
                        fragment = new EditProfileFragment();
                        break;
                    case 3:
                        //fragment = new EditProfileFragment();
                        startActivity(new Intent(NavigationDrawerActivity.this,PlateListActivity.class));
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = NavigationDrawerActivity.this.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.commit();
                    listview.setItemChecked(position, true);
                    listview.setSelection(position);
                    setTitle(nav_titles.get(position).getTitle());
                    mDrawerLayout.closeDrawer(leftView);
                } else {
                    Log.e(NavigationDrawerActivity.class.getSimpleName(), "Error creating Fragment");
                }
            }
        });
        getSupportActionBar().setIcon(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout ,R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
            // Handle action bar actions click
            switch (item.getItemId()) {
            /*case R.id.action_reverse:
               AlarmDialog alarmDialog = AlarmDialog.newInstance(getBaseContext());
                alarmDialog.show(getFragmentManager(), "AlarmDialog");*/
            default:
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(leftView);
        /**Date 17 June
         * As discussed once Alarm integration has been done we will show alarm function
         */
        //menu.findItem(R.id.alarm_icon).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void displayGenericFragment() {
        fragment = new ProductFragment();
        FragmentManager fragmentManager = NavigationDrawerActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }

    @Override
    public void setTitle(CharSequence title){
        super.setTitle(title);
        mTitle = title;
        Log.i(NavigationDrawerActivity.class.getSimpleName(), "" + mTitle);
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.getUserVisibleHint())
                return fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        /*Fragment fragment = getVisibleFragment();
        if(fragment instanceof LetsGoFragment){
            boolean backStatus = ((LetsGoFragment)fragment).backAction();
            if(!backStatus)
                return;
        }*/
        super.onBackPressed();
    }
}
