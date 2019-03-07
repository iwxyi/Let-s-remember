package com.iwxyi.letsremember;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.iwxyi.letsremember.Fragments.BoxFragment;
import com.iwxyi.letsremember.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private BoxFragment boxFragment;

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

                    break;
                case R.id.navigation_rank:

                    break;
                case R.id.navigation_group:

                    break;
                default :
                    return false;
            }
            ft.commit();
            return false;
        }
    };

    private void hideFragment(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (boxFragment != null) {
            ft.hide(boxFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        homeFragment = HomeFragment.newInstance("","");
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_layout, homeFragment);
    }

}
