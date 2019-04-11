package com.iwxyi.letsremember.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Material.CardsContent;
import com.iwxyi.letsremember.Material.PackagesContent;
import com.iwxyi.letsremember.Material.SectionsContent;
import com.iwxyi.letsremember.R;

import java.io.File;
import java.util.ArrayList;

public class NavTypeinFragment extends Fragment implements View.OnClickListener {

    private Spinner mPackageSp;
    private Spinner mSectionSp;
    private Spinner mCardSp;
    private EditText mTypeinEt;
    private Button mMyTypeinBtn;
    private Button mIntegralBtn;
    private Button mBalanceBtn;
    private Button mWithdrawalBtn;

    private String current_pacage;
    private String current_section;
    private String current_card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_typein, container, false);
        initView(view);
        return view;
    }

    public void refreshSpinner() {
        /*if (PackagesContent.ITEMS.size() == 0) {
            PackagesContent.refreshPackages();
        }
        if (SectionsContent.ITEMS.size() == 0) {
            SectionsContent.refreshSections(App.getVal("editing_package"));
        }
        if (CardsContent.ITEMS.size() == 0) {
            CardsContent.refreshCards(App.getVal("editing_package"), App.getVal("editing_section"));
        }*/

        // 刷新 Package
        String editing_package = App.getVal("editing_package");
        ArrayList<String> package_names = new ArrayList<>();
        File package_dir = new File(Paths.getLocalPath("material"));
        File[] package_files = package_dir.listFiles();
        for (int i = 0; i < package_files .length; i++) {
            package_names.add(package_files[i].getName());
        }
        ArrayAdapter<String> package_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, package_names);
        mPackageSp.setAdapter(package_adapter);

        // 刷新 Section
        String editing_section = App.getVal("editing_section");
        ArrayList<String> section_names = new ArrayList<>();
        File section_dir = new File(Paths.getLocalPath("material/"+current_pacage));
        File[] section_files = section_dir.listFiles();
        for (int i = 0; i < section_files .length; i++) {
            section_names.add(section_files[i].getName());
        }
        ArrayAdapter<String> section_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, section_names);
        mSectionSp.setAdapter(section_adapter);

        // 刷新 Cards

    }

    private void initView(@NonNull final View itemView) {
        mPackageSp = (Spinner) itemView.findViewById(R.id.sp_package);
        mSectionSp = (Spinner) itemView.findViewById(R.id.sp_section);
        mCardSp = (Spinner) itemView.findViewById(R.id.sp_card);
        mTypeinEt = (EditText) itemView.findViewById(R.id.et_typein);
        mMyTypeinBtn = (Button) itemView.findViewById(R.id.btn_my_typein);
        mMyTypeinBtn.setOnClickListener(this);
        mIntegralBtn = (Button) itemView.findViewById(R.id.btn_integral);
        mIntegralBtn.setOnClickListener(this);
        mBalanceBtn = (Button) itemView.findViewById(R.id.btn_balance);
        mBalanceBtn.setOnClickListener(this);
        mWithdrawalBtn = (Button) itemView.findViewById(R.id.btn_withdrawal);
        mWithdrawalBtn.setOnClickListener(this);

        refreshSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_typein:
                // TODO 19/04/11
                break;
            case R.id.btn_integral:
                // TODO 19/04/11
                break;
            case R.id.btn_balance:
                // TODO 19/04/11
                break;
            case R.id.btn_withdrawal:
                // TODO 19/04/11
                break;
            default:
                break;
        }
    }
}
