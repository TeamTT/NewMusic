package com.example.newmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newmusic.R;

/**
 * Created by Administrator on 2016/11/27.
 */
public class RadioFragment extends Fragment {

    public static final String TAG=RadioFragment.class.getSimpleName();

    private View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_radio,container,false);

        return layout;
    }
}
