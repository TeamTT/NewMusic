package com.example.newmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.adapters.LocationPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */
public class LocalFragment extends Fragment {

    public static final String TAG = LocalFragment.class.getSimpleName();

    private View layout;
    private TextView mEdit;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private LocationPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_local, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mEdit = ((TextView) layout.findViewById(R.id.teach_location_edit));
        mTablayout = ((TabLayout) layout.findViewById(R.id.teach_location_tablayout));
        mViewPager = ((ViewPager) layout.findViewById(R.id.teach_location_viewpager));
        adapter = new LocationPagerAdapter(getChildFragmentManager(),getFragments());
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new LocationMvFragment());
        fragments.add(new LocationRadioFragment());
        return fragments;
    }
}
