package com.example.newmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.newmusic.R;
import com.example.newmusic.adapters.LocationMvAdapter;
import com.example.newmusic.interfaces.ModeCallBack;
import com.example.newmusic.model.MvMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/11/27.
 */
public class LocationMvFragment extends Fragment{

    public static final String TAG = LocationMvFragment.class.getSimpleName();

    private View layout;
    private ListView mListView;
    private LocationMvAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MvMode mvMode) {
        Log.e(TAG, "onEvent: " + mvMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_location_mv, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        mListView = ((ListView) layout.findViewById(R.id.teach_location_mv_listview));
        adapter = new LocationMvAdapter(getContext(), null);
        mListView.setAdapter(adapter);
    }


}
