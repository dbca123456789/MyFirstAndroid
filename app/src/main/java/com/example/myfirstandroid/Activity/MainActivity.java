package com.example.myfirstandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfirstandroid.Fragment.MainFragment;
import com.example.myfirstandroid.Fragment.MineFragment;
import com.example.myfirstandroid.Fragment.SettingFragment;
import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";


    private LinearLayout ll_main, ll_setting, ll_mine;

    private MainFragment mainFragment;
    private SettingFragment settingFragment;
    private MineFragment mineFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    private ImageView img_main, img_seting, img_mine;
    private TextView text_main, text_setting, text_mine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();

        ll_main.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_mine.setOnClickListener(this);

        ApplicationUtil.getInstance().addActivity(this);
    }

    private void initView() {
        ll_main = findViewById(R.id.layout_main);
        ll_setting = findViewById(R.id.layout_setting);
        ll_mine = findViewById(R.id.layout_mine);

        img_main = findViewById(R.id.image_main);
        img_seting = findViewById(R.id.img_setting);
        img_mine = findViewById(R.id.img_mine);

        text_main = findViewById(R.id.text_main);
        text_setting = findViewById(R.id.text_setting);
        text_mine = findViewById(R.id.text_mine);

        text_main.setTextColor(Color.RED);
        img_main.setImageResource(R.drawable.main_selected);

    }

    private void initFragment() {
        mainFragment = new MainFragment();
        addFragment(mainFragment);
        showFragment(mainFragment);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()){
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.main_content, fragment).commit();
            fragmentList.add(fragment);
        }
    }

    private void showFragment(Fragment fragment) {

        for (Fragment frag: fragmentList){
            if (frag != fragment){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(frag).commit();
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.show(fragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_main:
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                addFragment(mainFragment);
                showFragment(mainFragment);

                text_main.setTextColor(Color.RED);
                text_setting.setTextColor(Color.BLACK);
                text_mine.setTextColor(Color.BLACK);

                img_main.setImageResource(R.drawable.main_selected);
                img_seting.setImageResource(R.drawable.setting);
                img_mine.setImageResource(R.drawable.mine);
                break;

            case R.id.layout_setting:
                if (settingFragment == null){
                    settingFragment = new SettingFragment();
                }
                addFragment(settingFragment);
                showFragment(settingFragment);

                text_setting.setTextColor(Color.RED);
                text_main.setTextColor(Color.BLACK);
                text_mine.setTextColor(Color.BLACK);

                img_seting.setImageResource(R.drawable.setting_selected);
                img_main.setImageResource(R.drawable.main);
                img_mine.setImageResource(R.drawable.mine);
                break;

            case R.id.layout_mine:
                if (mineFragment == null){
                    mineFragment = new MineFragment();
                }
                addFragment(mineFragment);
                showFragment(mineFragment);

                text_mine.setTextColor(Color.RED);
                text_main.setTextColor(Color.BLACK);
                text_setting.setTextColor(Color.BLACK);

                img_mine.setImageResource(R.drawable.mine_selected);
                img_main.setImageResource(R.drawable.main);
                img_seting.setImageResource(R.drawable.setting);
                break;

            default:
                break;
        }
    }
}