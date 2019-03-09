package com.iwxyi.letsremember;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.iwxyi.letsremember.Fragments.BoxFragment;
import com.iwxyi.letsremember.Fragments.GroupFragment;
import com.iwxyi.letsremember.Fragments.HomeFragment;
import com.iwxyi.letsremember.Fragments.RankFragment;
import com.iwxyi.letsremember.Fragments.TypeinFragment;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private BoxFragment boxFragment;
    private TypeinFragment typeinFragment;
    private RankFragment rankFragment;
    private GroupFragment groupFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hideFragment(ft);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        ft.add(R.id.frame_layout, homeFragment, "home");
                    } else {
                        ft.show(homeFragment);
                    }
                    break;
                case R.id.navigation_box:
                    if (boxFragment == null) {
                        boxFragment = new BoxFragment();
                        ft.add(R.id.frame_layout, boxFragment, "home");
                    } else {
                        ft.show(boxFragment);
                    }
                    break;
                case R.id.navigation_typein:
                    if (typeinFragment == null) {
                        typeinFragment = new TypeinFragment();
                        ft.add(R.id.frame_layout, typeinFragment, "typein");
                    } else {
                        ft.show(typeinFragment);
                    }
                    break;
                case R.id.navigation_rank:
                    if (rankFragment == null) {
                        rankFragment = new RankFragment();
                        ft.add(R.id.frame_layout, rankFragment, "rank");
                    } else {
                        ft.show(rankFragment);
                    }
                    break;
                case R.id.navigation_group:
                    if (groupFragment == null) {
                        groupFragment = new GroupFragment();
                        ft.add(R.id.frame_layout, groupFragment, "group");
                    } else {
                        ft.show(groupFragment);
                    }
                    break;
                default :
                    return false;
            }
            ft.commit();
            return true;
        }
    };

    private void hideFragment(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (boxFragment != null) {
            ft.hide(boxFragment);
        }
        if (typeinFragment != null) {
            ft.hide(typeinFragment);
        }
        if (rankFragment != null) {
            ft.hide(rankFragment);
        }
        if (groupFragment != null) {
            ft.hide(groupFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragment();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_layout, homeFragment, "home").commit();
    }

}
