package com.example.newmusic;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.newmusic.fragment.CommentFragment;
import com.example.newmusic.fragment.IntroduceFragment;
import com.example.newmusic.fragment.MvPagerAdapter;
import com.example.newmusic.model.MvMode;

import java.util.ArrayList;
import java.util.List;

public class MvActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MvActivity.class.getSimpleName();
    private MvMode mvMode;
    private TextView mBack;
    private VideoView mVideoView;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private MvPagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mv);
        getMv();
        initView();
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.teach_mv_videoview);
        mVideoView.setVideoURI(Uri.parse(mvMode.getUrl()));
        mVideoView.start();
        mBack = (TextView) findViewById(R.id.teach_mv_back);
        mBack.setOnClickListener(this);
        mTablayout = (TabLayout) findViewById(R.id.teach_mv_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.teach_mv_viewpage);
        mPageAdapter = new MvPagerAdapter(getSupportFragmentManager(), getFragment());
        mViewPager.setAdapter(mPageAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }

    private List<Fragment> getFragment() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new IntroduceFragment());
        fragments.add(new CommentFragment());
        return fragments;
    }

    private void getMv() {
        Intent intent = getIntent();
        mvMode = ((MvMode) intent.getParcelableExtra("mv"));
        Log.e(TAG, "getMv: " + mvMode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mVideoView = null;
        }
    }
}
