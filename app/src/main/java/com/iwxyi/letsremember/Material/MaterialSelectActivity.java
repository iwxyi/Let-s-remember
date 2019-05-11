package com.iwxyi.letsremember.Material;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Users.SettingsActivity;

public class MaterialSelectActivity extends AppCompatActivity implements
        PackagesFragment.OnPackagesFragmentInteractionListener,
        SectionsFragment.OnSectionsFragmentInteractionListener,
        RememberFragment.OnRememberFragmentInteractionListener,
        CardsFragment.OnCardsFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private PackagesFragment packagesFragment;
    private SectionsFragment sectionsFragment;
    private RememberFragment rememberFragment;
    private CardsFragment cardsFragment;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("选择记忆包");

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            if (intent.getBooleanExtra("open", false) == true) {
                mViewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_material_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MaterialSelectActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择记忆包
     * @param item
     */
    @Override
    public void onPackageClicked(PackagesContent.PackageItem item) {
        String package_name = item.content;
        App.setVal("selected_package", package_name);

        // 刷新 sectionFragment 显示的列表
        if (sectionsFragment != null) {
            sectionsFragment.refreshSections(package_name);
        }

        mViewPager.setCurrentItem(1);
    }

    /**
     * 选择记忆包中的某个章节
     * @param item
     */
    @Override
    public void onSectionClicked(SectionsContent.SectionItem item) {
        String section_name = item.content;
        App.setVal("selected_section", section_name);

        App.setVal("last_package", App.getVal("selected_package"));
        App.setVal("last_section", section_name);

        if (rememberFragment != null) {
            rememberFragment.initData();
        }
        if (cardsFragment != null) {
            cardsFragment.refreshCards(App.getVal("selected_package"), section_name);
        }

        mViewPager.setCurrentItem(2);
    }

    @Override
    public void onRememberFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCardRemember(int index) {
        if (cardsFragment != null) {
            cardsFragment.refreshOneCard(index);
        }
    }

    @Override
    public void onCardClicked(CardsContent.CardItem item) {

    }

    @Override
    public void onCardClicked(int index) {
        App.setVal("selected_card", index);
        App.setVal("last_card", index);

        if (rememberFragment != null) {
            rememberFragment.setChapter(index);
        }
        mViewPager.setCurrentItem(2);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_material_select, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("hello");
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return packagesFragment = PackagesFragment.newInstance();
                case 1:
                    return sectionsFragment = SectionsFragment.newInstance();
                case 2:
                    return rememberFragment = RememberFragment.newInstance(
                            App.getVal("selected_package"), App.getVal("selected_section"));
                case 3:
                    return cardsFragment = CardsFragment.newInstance();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        /**
         * 切换页面时，修改Activity的标题
         * @param i 页面索引，从0开始
         */
        @Override
        public void onPageSelected(int i) {
            if (toolbar == null) {
                toolbar = (Toolbar) findViewById(R.id.toolbar);
            }
            switch (i) {
                case 0:

                    if (toolbar != null) {
                        toolbar.setTitle("选择记忆包");
                    }
                    break;
                case 1:
                    fab.show();
                    if (toolbar != null) {
                        toolbar.setTitle("记忆包："+App.getVal("selected_package"));
                    }
                    break;
                case 2:
                    fab.hide();
                    if (toolbar != null) {
                        toolbar.setTitle(App.getVal("last_section"));
                    }
                    break;
                case 3:
                    fab.hide();
                    if (toolbar != null) {
                        toolbar.setTitle(App.getVal("selected_section")+" 目录");
                    }
                    if (cardsFragment != null) {
                        cardsFragment.refreshCards(); // 切换时刷新列表
                    }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

}
