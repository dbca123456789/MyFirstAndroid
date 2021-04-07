package com.example.myfirstandroid.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myfirstandroid.Adapter.FragmentAdapter;
import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.TimeCount;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FragmentAdapter fragmentAdapter;

    private EnteFragment enteFragment;
    private MiliFragment miliFragment;
    private SportFragment sportFragment;
    private TechFragment techFragment;

    List<String> titleList;
    List<Fragment> fragmentList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        fragmentChange();
        TimeCount.getInstance().setTime(System.currentTimeMillis());
        return view;
    }

    private void initView() {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
    }

    private void fragmentChange() {
        fragmentList = new ArrayList<>();

        enteFragment = new EnteFragment();
        miliFragment = new MiliFragment();
        sportFragment = new SportFragment();
        techFragment = new TechFragment();

        fragmentList.add(techFragment);
        fragmentList.add(enteFragment);
        fragmentList.add(sportFragment);
        fragmentList.add(miliFragment);


        titleList = new ArrayList<>();
        titleList.add("科技");
        titleList.add("娱乐");
        titleList.add("运动");
        titleList.add("军事");

        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentAdapter);

        //将tabLayout与viewPager连起来
        tabLayout.setupWithViewPager(viewPager);
    }


}
