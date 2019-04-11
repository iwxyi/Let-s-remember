package com.iwxyi.letsremember.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private ArrayList<String> package_names;
    private ArrayList<String> section_names;
    private ArrayList<String> card_names;
    private String current_package; // 当前记忆包名
    private String current_section; // 当前章节名
    private int current_card;       // 当前卡片名

    private String cards_mid;   // 当前章节的默认内容，如果没有改变就不保存
    private boolean cards_changed = false; // 当前章节是否已经进行了改变
    private String cards_left;  // 当前章节左边的文本
    private String cards_right; // 当前章节右边的文本

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

        mPackageSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_package = package_names.get(position);
                App.setVal("editing_package", current_package);
                refreshSpinner(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSectionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_section = section_names.get(position);
                App.setVal("editing_section", current_section);
                refreshSpinner(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCardSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_card = position;
                App.setVal("editing_card", current_card);
                refreshSpinner(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTypeinEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (current_package.isEmpty() || current_section.isEmpty() || current_card == -1) {
                    return ;
                }
                if (s.toString().equals(cards_mid) && !cards_changed) {
                    App.deb("没有改变");
                    return ;
                }
                String card = s.toString();
                card = "<card><content>"+card+"</content></card>";
                String full = cards_left + card + cards_right;
                String path = "material/"+current_package+"/"+current_section+".txt";
                FileUtil.writeTextVals(path, full);
                cards_changed = true;
                App.deb("text change:"+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshSpinner(3);
    }

    public void refreshSpinner(int level) {
        if (level >= 3) {
            // 刷新 Package
            refreshPackageSpinner();
        }
        if (level >= 2) {
            // 刷新 Section
            refreshSectionSpinner();
        }

        if (level >= 1) {
            // 刷新 Cards
            refreshCardSpinner();
        }

        if (level >= 0) {
            // 刷新录入框内容
            refreshTypeinContent();
        }
    }

    public void refreshPackageSpinner() {
        // 设置列表
        package_names = new ArrayList<>();
        File package_dir = new File(Paths.getLocalPath("material"));
        File[] package_files = package_dir.listFiles();
        for (int i = 0; i < package_files.length; i++) {
            package_names.add(package_files[i].getName());
        }
        ArrayAdapter<String> package_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, package_names);
        mPackageSp.setAdapter(package_adapter);

        // 设置当前默认值
        String editing_package = App.getVal("editing_package");
        if (!editing_package.isEmpty() && package_names.contains(editing_package)) {
            for (int i = 0; i < package_names.size(); i++) {
                if (editing_package.equals(package_names.get(i))) {
                    mPackageSp.setSelection(i);
                    break;
                }
            }
            current_package = editing_package;
        } else if (package_names.size() > 0) {
            current_package = package_names.get(0);
        } else {
            Toast.makeText(getContext(), "找不到记忆包，请检查是否禁止了APP的读取存储权限？", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshSectionSpinner() {
        if (current_package.isEmpty()) {
            current_section = "";
            return;
        }

        // 设置列表
        section_names = new ArrayList<>();
        File section_dir = new File(Paths.getLocalPath("material/" + current_package));
        File[] section_files = section_dir.listFiles();
        for (int i = 0; i < section_files.length; i++) {
            String fileName = section_files[i].getName();
            if (fileName.endsWith(".txt")) {
                fileName = fileName.substring(0, fileName.length() - 4);
            }
            section_names.add(fileName);
        }
        ArrayAdapter<String> section_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, section_names);
        mSectionSp.setAdapter(section_adapter);

        // 设置默认值
        String editing_section = App.getVal("editing_section");
        if (!editing_section.isEmpty() && section_names.contains(editing_section)) {
            for (int i = 0; i < section_names.size(); i++) {
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
            current_card = -1;
            return;
        }

        // 设置列表
        card_names = new ArrayList<>();
        String path = "material/" + current_package + "/" + current_section + ".txt";
        String content = FileUtil.readTextVals(path);
        if (content.isEmpty()) {
            App.err("无法读取文件：" + path);
            return;
        }
        ArrayList<String> cards = StringUtil.getXmls(content, "card");
        for (int i = 0; i < cards.size(); i++) {
            String title = StringUtil.getXml(cards.get(i), "content").trim();
            title = getContentTitle(title);
            card_names.add("" + (i + 1) + ". " + title);
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

    /**
     * 刷新录入框的内容
     */
    public void refreshTypeinContent() {
        String path = "material/" + current_package + "/" + current_section + ".txt";
        String content = FileUtil.readTextVals(path);
        if (content.isEmpty()) {
            App.err("无法读取文件：" + path);
            return;
        }
        ArrayList<String> cards = StringUtil.getXmls(content, "card");

        // 判断索引
        int old_index = current_card;
        if (current_card < 0) {
            current_card = 0;
        }
        if (current_card >= cards.size()) {
            current_card = cards.size() - 1;
        }
        if (old_index != current_card) {
            mCardSp.setSelection(current_card);
        }

        // 设置当前索引
        String card = cards.get(current_card).trim();
        String con = StringUtil.getXml(card, "content");
        setTypeinDef(con);
        mTypeinEt.setText(con);

        // 设置保存的前后文本(增强性能)
        cards_left = cards_right = "";
        for (int i = 0; i < current_card; i++) {
            cards_left += cards.get(i);
        }
        for (int i = current_card+1; i < cards .size(); i++) {
            cards_right += cards.get(i);
        }
    }

    private void setTypeinDef(String def) {
        cards_mid = def;
        cards_changed = false;
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
