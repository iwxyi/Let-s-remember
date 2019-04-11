package com.iwxyi.letsremember.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

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
    private int current_card;

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

    public void refreshAllSpinner() {
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
        refreshPackageSpinner();

        // 刷新 Section
        refreshSectionSpinner();

        // 刷新 Cards
        refreshCardSpinner();
    }

    public void refreshPackageSpinner() {
        // 设置列表
        ArrayList<String> package_names = new ArrayList<>();
        File package_dir = new File(Paths.getLocalPath("material"));
        File[] package_files = package_dir.listFiles();
        for (int i = 0; i < package_files .length; i++) {
            package_names.add(package_files[i].getName());
        }
        ArrayAdapter<String> package_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, package_names);
        mPackageSp.setAdapter(package_adapter);

        // 设置当前默认值
        String editing_package = App.getVal("editing_package");
        if (!editing_package.isEmpty() && package_names.contains(editing_package)) {
            for (int i = 0; i < package_names .size(); i++) {
                if (editing_package.equals(package_names.get(i))) {
                    mPackageSp.setSelection(i);
                    break;
                }
            }
            current_pacage = editing_package;
        } else if (package_names.size() > 0) {
            current_pacage = package_names.get(0);
        } else {
            Toast.makeText(getContext(), "找不到记忆包，请检查是否禁止了APP的读取存储权限？", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshSectionSpinner() {
        if (current_pacage.isEmpty()) {
            current_section = "";
            return ;
        }

        // 设置列表
        ArrayList<String> section_names = new ArrayList<>();
        File section_dir = new File(Paths.getLocalPath("material/"+current_pacage));
        File[] section_files = section_dir.listFiles();
        for (int i = 0; i < section_files .length; i++) {
            String fileName = section_files[i].getName();
            if (fileName.endsWith(".txt")) {
                fileName = fileName.substring(0, fileName.length()-4);
            }
            section_names.add(fileName);
        }
        ArrayAdapter<String> section_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, section_names);
        mSectionSp.setAdapter(section_adapter);

        // 设置默认值
        String editing_section = App.getVal("editing_section");
        if (!editing_section.isEmpty() && section_names.contains(editing_section)) {
            for (int i = 0; i < section_names .size(); i++) {
                if (editing_section.equals(section_names.get(i))) {
                    mSectionSp.setSelection(i);
                    break;
                }
            }
            current_section = editing_section;
        } else if (section_names.size() > 0) {
            current_section = section_names.get(0);
        }
    }

    public void refreshCardSpinner() {
        if (current_section.isEmpty()) {
            current_card = 0;
            return ;
        }

        // 设置列表
        ArrayList<String> card_names = new ArrayList<>();

        String path = "material/" + current_pacage + "/" + current_section + ".txt";
        String content = FileUtil.readTextVals(path);
        if (content.isEmpty()) {
            App.err("无法读取文件：" + path);
            return ;
        }
        ArrayList<String> cards = StringUtil .getXmls(content, "card");
        for (int i = 0; i < cards .size(); i++) {
            String title = StringUtil.getXml(cards.get(i), "content");
            title = getContentTitle(title);
            card_names.add(""+(i+1)+". "+title);
        }
        ArrayAdapter<String> card_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, card_names);
        mCardSp.setAdapter(card_adapter);

        // 设置默认值
        int editing_card = App.getInt("editing_card");
        if (editing_card > 0 && editing_card < card_names.size()) {
            current_card = editing_card;
        } else {
            current_card = 0;
        }
    }

    private static String getContentTitle(String c) {
        String ans = c;
        int pos = c.indexOf("\n");
        if (pos > -1) {
            ans = c.substring(0, pos);
        }
        if (ans.length() > 20)
            ans = ans.substring(0, 20);
        return ans;
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

        refreshAllSpinner();
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
