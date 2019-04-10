package com.iwxyi.letsremember.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Material.PackagesContent;
import com.iwxyi.letsremember.Material.SectionsContent;
import com.iwxyi.letsremember.Material.SectionsFragment;
import com.iwxyi.letsremember.R;

import java.util.ArrayList;

public class NavTypeinFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSpinner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_typein, container, false);
    }

    public void refreshSpinner() {
        if (PackagesContent.ITEMS.size() == 0) {
            PackagesContent.refreshPackages();
        }
        if (SectionsContent.ITEMS.size() == 0) {
            SectionsContent.refreshSections(App.getVal("last_package"));;
        }
    }
}
