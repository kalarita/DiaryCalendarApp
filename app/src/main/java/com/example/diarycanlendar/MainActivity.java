package com.example.diarycanlendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diarycanlendar.ui.adapter.fragmentadapter;
import com.example.diarycanlendar.ui.fragment.home_fragment;
import com.example.diarycanlendar.ui.fragment.todayinhis_fragment;
import com.example.diarycanlendar.ui.fragment.todayinmyhis_fragment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //获取工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取悬浮按钮
        File filePictures = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        FloatingActionsMenu fab = findViewById(R.id.fab_menu);

        FloatingActionButton button_a = new FloatingActionButton(getBaseContext());
        button_a.setTitle("添加日记");
        button_a.setColorNormal(R.color.fab_color);
        button_a.setColorDisabled(R.color.fab_color);
        button_a.setIcon(R.drawable.ic_collections_bookmark_black_36dp);

        fab.addButton(button_a);
        FloatingActionButton button_b = new FloatingActionButton(getBaseContext());
        button_b.setTitle("添加日程");
        button_b.setColorNormal(R.color.fab_color);
        button_b.setIcon(R.drawable.ic_today_black_36dp);
        button_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"添加日记",Toast.LENGTH_SHORT).show();
            }
        });
        button_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"添加日程",Toast.LENGTH_SHORT).show();
            }
        });
        fab.addButton(button_b);
        //抽屉布局
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //导航获取
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //侧边栏用户账号
        View head_view = navigationView.getHeaderView(0);
        TextView accountText = head_view.findViewById(R.id.nav_textView);
        SharedPreferences sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        String accountstr = sharedPreferences.getString("account","未登录!");
        accountText.setText(accountstr);


        //填充首页
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new home_fragment());
        fragments.add(new todayinhis_fragment());
        fragments.add(new todayinmyhis_fragment());
        fragmentadapter mFragmentAdapter = new fragmentadapter(getSupportFragmentManager(), fragments);
        ViewPager mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);

    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}