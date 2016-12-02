package com.example.newmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MusicDetailViewpageAdapter extends FragmentPagerAdapter {

    private List<Fragment>data;

    public MusicDetailViewpageAdapter(FragmentManager fm,List<Fragment>data) {
        super(fm);
        if (data != null) {
            this.data=data;
        }else{
            this.data=new ArrayList<>();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
