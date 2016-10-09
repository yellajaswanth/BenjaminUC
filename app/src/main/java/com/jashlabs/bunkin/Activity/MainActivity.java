package com.jashlabs.bunkin.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jashlabs.bunkin.Adapter.SectionsPagerAdapter;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Services.PubnubService;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;


public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    SharedPreferences sharedPreferences;
    private String username;
    private String channel;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);

        if (sharedPreferences.getString(Constants.PREF_USER_EMAIL, null) == null) {
            this.username = sharedPreferences.getString(Constants.PREF_NAME, Constants.PREF_USER_EMAIL);
            this.channel = sharedPreferences.getString(Constants.PREF_NAME, Constants.PREF_USER_CHANNEL);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else{
            startService(new Intent(MainActivity.this, PubnubService.class));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
