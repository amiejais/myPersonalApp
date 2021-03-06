package com.myapplock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.myapplock.R;
import com.myapplock.adapter.TabsPagerAdapter;
import com.myapplock.framework.api.UpdateListContent;
import com.myapplock.service.LaunchDetectorService;
import com.myapplock.ui.fragment.AboutUsFragment;
import com.myapplock.ui.fragment.AllAppsLandingFragment;
import com.myapplock.ui.fragment.ImageVaultFragment;
import com.myapplock.ui.fragment.SettingFragment;
import com.myapplock.ui.fragment.VideoVaultFragment;
import com.myapplock.utils.CommonUtils;
import com.myapplock.utils.MyAppLockConstansts;
import com.myapplock.utils.MyAppLockPreferences;

import java.util.ArrayList;
import java.util.List;

public class LaunchingHiddenAppActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FrameLayout mContainer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsPagerAdapter mAdapter;

    private boolean isHomeScreen = true;
    private String Titles[] = {"UNLOCKED APPS", "LOCKED APPS"};
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTitle = getTitle();
        initViews();

    }

    private void initViews() {
        mContainer = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkFirstInstall();
        initNavigationView();
        initViewPager();
        initDrawer();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                if (drawerLayout != null)
                    drawerLayout.closeDrawers();
                navigateTo(menuItem.getItemId());
                return true;
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllAppsLandingFragment());

        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments, Titles);
        viewPager.setAdapter(mAdapter);

        initTabs();
        changeTitle(0);
    }

    private void initTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CommonUtils.showToast(LaunchingHiddenAppActivity.this, "" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                ((UpdateListContent) mAdapter.getItem(tab.getPosition())).updateList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    public void navigateTo(int position) {
        switch (position) {
            case R.id.home:
                hideShowViews(true);
                changeTitle(0);
                break;
            case R.id.image_vault:
                changeFragment(new ImageVaultFragment(), "Image Vault", false, 1);
                break;
            case R.id.video_vault:
                changeFragment(new VideoVaultFragment(), "Video Vault", false, 2);
                break;
            case R.id.settings:
                changeFragment(new SettingFragment(), "Setting", false, 3);
                break;
            case R.id.about_us:
                changeFragment(new AboutUsFragment(), "About Us", false, 4);
                break;
        }

    }

    private void changeFragment(Fragment pFragment, String pFragmentName, boolean pShow, int pos) {
        hideShowViews(pShow);
        changeTitle(pos);
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(pFragmentName)
                .replace(R.id.container, pFragment).commit();
    }

    private void hideShowViews(boolean show) {
        if (show) {
            isHomeScreen = true;
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.GONE);
        } else {
            isHomeScreen = false;
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            mContainer.setVisibility(View.VISIBLE);
        }
        supportInvalidateOptionsMenu();
    }

    public void changeTitle(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.home);
                break;
            case 1:
                mTitle = getString(R.string.image_vault);
                break;
            case 2:
                mTitle = getString(R.string.video_vault);
                break;
            case 3:
                mTitle = getString(R.string.settings);
                break;
            case 4:
                mTitle = getString(R.string.about_us);
                break;
        }
        toolbar.setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        getMenuInflater().inflate(R.menu.menu, menu);

        if (!isHomeScreen) {
            item = menu.findItem(R.id.select_all);
            item.setVisible(false);
            item = menu.findItem(R.id.deSelect_all);
            item.setVisible(false);
            item = menu.findItem(R.id.refresh);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.deSelect_all || super.onOptionsItemSelected(item);
    }

    private void checkFirstInstall() {
        boolean isRunning = CommonUtils.isMyServiceRunning(this, LaunchDetectorService.class);
        if (!isRunning) {
            startService(new Intent(this, LaunchDetectorService.class));
        }
        if (!MyAppLockPreferences.getBoolFromPref(this, MyAppLockConstansts.PREF_FIRST_INSTALL_COMPLETE,false))
        {
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_FIRST_INSTALL_COMPLETE, true);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_SERVICE_ENABLED, isRunning);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_CURRENT_LOCK_MODE, false);
            MyAppLockPreferences.saveStrToPref(this, MyAppLockConstansts.PREF_PASSWORD, "0000");
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_AUTO_START, true);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_VIBRATE, false);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_LOCKPATTERN_VISIBLE, true);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_RELOCKPOLICY_ENABLE, false);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_RELOCKPOLICY_ONSCREEN_ON, false);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_HIDE_APPLOCK_FROM_HOME, false);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_LOCK_NEW_APP, false);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_HIDE_IMAGES_FROM_GALLERY, true);
            MyAppLockPreferences.saveBoolToPref(this, MyAppLockConstansts.PREF_SHOW_NOTIFICATION_BAR, false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        requestCode &= 0xffff;
        super.onActivityResult(requestCode, resultCode, data);
        if (MyAppLockConstansts.cuurentFragment.equalsIgnoreCase("AllAppsLandingFragment")) {
            Fragment generalSettingFragment = getSupportFragmentManager().getFragments().get(0);
            if (generalSettingFragment != null) {
                generalSettingFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {

            Fragment settingFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (settingFragment != null) {
                Fragment generalSettingFragment = settingFragment.getParentFragment();
                if (generalSettingFragment != null) {
                    generalSettingFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

    }

    /**
     * get Active Fragment from BackStack
     */
    public Fragment getActiveFragment()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
