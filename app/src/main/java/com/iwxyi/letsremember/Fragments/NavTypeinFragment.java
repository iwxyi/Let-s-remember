package com.iwxyi.letsremember.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Material.CardBean;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.InputDialog;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

public class NavTypeinFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final int LEVEL_PACKAGE = 3;
    private static final int LEVEL_SECTION = 2;
    private static final int LEVEL_CARD = 1;
    private static final int LEVEL_TYPEIN = 0;

    private Spinner mPackageSp;
    private Spinner mSectionSp;
    private Spinner mCardSp;
    private EditText mTypeinEt;
    private Button mHistoryBtn;
    private Button mCountBtn;
    private Button mBalanceBtn;
    private Button mWithdrawalBtn;
    private ImageButton mMenuBtn;

    private ArrayList<String> package_names;
    private ArrayList<String> section_names;
    private ArrayList<String> card_names;
    private String current_package;    // 当前记忆包名
    private String current_section;    // 当前章节名
    private int current_card_index; // 当前卡片名

    private CardBean current_card_bean;         // 当前卡片JavaBean(用来存放其他数据)
    private String cards_mid;                 // 当前章节的默认内容，如果没有改变就不保存
    private boolean cards_changed = false; // 当前章节是否已经进行了改变
    private String cards_left;                // 当前章节左边的文本
    private String cards_right;               // 当前章节右边的文本
    private ArrayAdapter<String> card_adapter;

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
        mHistoryBtn = (Button) itemView.findViewById(R.id.btn_typein_history);
        mHistoryBtn.setOnClickListener(this);
        mCountBtn = (Button) itemView.findViewById(R.id.btn_type_count);
        mCountBtn.setOnClickListener(this);
        mMenuBtn = (ImageButton) itemView.findViewById(R.id.btn_menu);
        mMenuBtn.setOnClickListener(this);

        refreshSpinner(3);

        mPackageSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (current_package.equals(package_names.get(position))) {
                    return;
                }
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
                if (current_section.equals(section_names.get(position))) {
                    return;
                }
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
                if (current_card_index == position) {
                    return;
                }
                current_card_index = position;
                App.setVal("editing_card", current_card_index);
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

            /**
             * 录入框文本改变事件
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (current_package.isEmpty() || current_section.isEmpty() || current_card_index == -1) {
                    return;
                }
                if (s.toString().equals(cards_mid) && !cards_changed) {
                    return;
                }

                // 保存到文件
                String card_content = s.toString();
                current_card_bean.setContent(card_content);
                String full = cards_left + current_card_bean.toString() + cards_right;
                String path = "material/" + current_package + "/" + current_section + ".txt";
                FileUtil.writeTextVals(path, full);
                cards_changed = true;

                // 保存下拉列表框的内容（不是很必要，会损失性能）
                if (current_card_index < 0 || current_card_index >= card_names.size()) {
                    App.err("记忆卡片索引出错");
                    return ;
                }
                String old_title = card_names.get(current_card_index);
                String new_title = "" + (current_card_index + 1) + ". " + getContentTitle(card_content);
                if (!old_title.equals(new_title)) {
                    card_names.set(current_card_index, new_title);
                    card_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        current_package = current_section = "";
        current_card_index = -1;

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
        current_section = "";
        current_card_index = -1;
        if (current_package.isEmpty()) {
            section_names = new ArrayList<>();
            ArrayAdapter<String> section_adapter = new ArrayAdapter<>
                    (getContext(), android.R.layout.simple_spinner_item, section_names);
            mSectionSp.setAdapter(section_adapter);
            mTypeinEt.setText("");
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
        current_card_index = -1;
        if (current_section.isEmpty()) {
            card_names = new ArrayList<>();
            card_adapter = new ArrayAdapter<>
                    (getContext(), android.R.layout.simple_spinner_item, card_names);
            mCardSp.setAdapter(card_adapter);
            mTypeinEt.setText("");
            return;
        }

        // 设置列表
        card_names = new ArrayList<>();
        String path = "material/" + current_package + "/" + current_section + ".txt";
        String content = FileUtil.readTextVals(path);

        ArrayList<String> cards = StringUtil.getXmls(content, "card");
        for (int i = 0; i < cards.size(); i++) {
            String title = StringUtil.getXml(cards.get(i), "content").trim();
            title = getContentTitle(title);
            card_names.add("" + (i + 1) + ". " + title);
        }
        card_adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, card_names);
        mCardSp.setAdapter(card_adapter);

        // 设置默认值
        int editing_card = App.getInt("editing_card");
        if (editing_card >= 0 && editing_card < card_names.size()) {
            current_card_index = editing_card;
        } else if (editing_card >= card_names.size()) {
            current_card_index = card_names.size()-1;
        } else if (card_names.size() > 0) {
            current_card_index = 0;
        } else { // 如果没有索引
            current_card_index = -1;
            return ;
        }
        if (current_card_index > 0) // ==0 不需要手动设置了
            mCardSp.setSelection(current_card_index);
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
        if (current_package.isEmpty() || current_section.isEmpty()) {
            return ;
        }

        String path = "material/" + current_package + "/" + current_section + ".txt";
        String content = FileUtil.readTextVals(path);
        ArrayList<String> cards = StringUtil.getXmls(content, "card");

        // 判断索引
        int old_index = current_card_index;
        if (current_card_index < 0) {
            current_card_index = 0;
        }
        if (current_card_index >= cards.size()) {
            current_card_index = cards.size() - 1;
        }
        if (current_card_index < 0) {
            mTypeinEt.setText("");
            return ;
        }
        if (old_index != current_card_index) {
            mCardSp.setSelection(current_card_index);
        }

        // 设置当前索引
        String card = cards.get(current_card_index).trim();
        current_card_bean = new CardBean(card);
        String con = current_card_bean.getContent();
        setTypeinDef(con);
        mTypeinEt.setText(con);

        // 设置保存的前后文本(增强性能)
        cards_left = cards_right = "";
        for (int i = 0; i < current_card_index; i++) {
            cards_left += "<card>" + cards.get(i) + "</card>";
        }
        for (int i = current_card_index + 1; i < cards.size(); i++) {
            cards_right += "<card>" + cards.get(i) + "</card>";
        }
    }

    private void setTypeinDef(String def) {
        cards_mid = def;
        cards_changed = false;
    }

    private void saveTypein() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_typein_history:
                // TODO 19/04/11
                break;
            case R.id.btn_type_count:
                // TODO 19/04/11
                break;
            case R.id.btn_menu:
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_typein, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_package:
                InputDialog.inputDialog(getContext(), "请输入记忆包名字", "", new StringCallback(){
                    @Override
                    public void onFinish(String content) {
                        if (FileUtil.exist(Paths.getLocalPath("material/" + content))) {
                            Toast.makeText(getContext(), "同名记忆包已存在", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        FileUtil.ensureFolder("material/"+content);
                        refreshPackageSpinner();
                    }
                });
                break;
            case R.id.add_section:
                if (current_package.isEmpty()) {
                    App.err("请先添加一个记忆包");
                    return false;
                }
                InputDialog.inputDialog(getContext(), "请输入记忆章节名字", "", new StringCallback(){
                    @Override
                    public void onFinish(String content) {
                        if (FileUtil.exist(Paths.getLocalPath("material/" + current_package + "/" + content + ".txt"))) {
                            Toast.makeText(getContext(), "同名记忆章节已存在", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        FileUtil.ensureFile("material/" + current_package + "/" + content + ".txt");
                        refreshSectionSpinner();
                    }
                });
                break;
            case R.id.add_card:
                if (current_package.isEmpty() || current_section.isEmpty()) {
                    App.err("请选择记忆包和章节");
                    return false;
                }
                current_card_index++;
                App.setVal("editing_card", current_card_index);
                if (current_card_bean != null) {
                    cards_left += current_card_bean.toString();
                }
                current_card_bean = new CardBean("");
                String full = cards_left + current_card_bean.toString() + cards_right;
                String path = "material/" + current_package + "/" + current_section + ".txt";
                FileUtil.writeTextVals(path, full);
//                refreshCardSpinner();
                refreshSpinner(1);
                mTypeinEt.requestFocus();
                User.addTypeinCount();
                mCountBtn.setText("数量：" + User.typeinCount);
                break;
            case R.id.delete_package:
                if (current_package.isEmpty()) {
                    App.err("删除失败，没有选择记忆包");
                    return false;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("是否删除当前记忆包？\n此操作将无法恢复，请慎重考虑")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileUtil.delete(Paths.getLocalPath("material/" + current_package));
                                refreshSpinner(LEVEL_PACKAGE);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.delete_section:
                if (current_section.isEmpty()) {
                    App.err("删除失败，没有选中章节");
                    return false;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("是否删除当前章节？\n此操作将无法恢复，请慎重考虑")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileUtil.delete(Paths.getLocalPath("material/" + current_package + "/" + current_section + ".txt"));
                                refreshSpinner(LEVEL_SECTION);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.delete_card:
                if (current_card_index == -1) {
                    App.err("删除失败，没有选中记忆卡片");
                    return false;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("是否删除当前记忆卡片？\n此操作将无法恢复，请慎重考虑")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 保存到文件
                                String full = cards_left + cards_right;
                                String path = "material/" + current_package + "/" + current_section + ".txt";
                                FileUtil.writeTextVals(path, full);

                                // 保存下拉列表框的内容（不是很必要，会损失性能）
                                card_names.remove(current_card_index);
                                if (current_card_index >= card_names.size())
                                    current_card_index--;
                                App.setVal("editing_card", current_card_index);
                                card_adapter.notifyDataSetChanged();
                                refreshSpinner(LEVEL_CARD);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            default:
                break;
        }

        return false;
    }
}
