package com.example.newmusic.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.model.MvMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/27.
 */
public class IntroduceFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = IntroduceFragment.class.getSimpleName();

    private View layout;
    private TextView mContent;
    private TextView mData;
    private TextView mNumber;
    private TextView mShare;
    private TextView mReward;
    private TextView mDownLoad;
    private MvMode mvMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_introduce, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMv();
        initView();
        setupView();
    }

    private void getMv() {
        Intent intent = getActivity().getIntent();
        mvMode = ((MvMode) intent.getParcelableExtra("mv"));
        Log.e(TAG, "getMv: " + mvMode);
    }

    private void setupView() {
        mContent.setText(mvMode.getTitle());
        mNumber.setText(Integer.parseInt(mvMode.getViews()) / 1000 + "万");
        String format = new SimpleDateFormat("yyyy.MM.dd").format(new Date(Long.parseLong("1478585487")));
        mData.setText(format);
    }

    private void initView() {
        mContent = (TextView) layout.findViewById(R.id.teach_mv_content);
        mData = (TextView) layout.findViewById(R.id.teach_mv_data);
        mNumber = (TextView) layout.findViewById(R.id.teach_mv_number);
        mShare = (TextView) layout.findViewById(R.id.teach_mv_share);
        mReward = (TextView) layout.findViewById(R.id.teach_mv_reward);
        mDownLoad = (TextView) layout.findViewById(R.id.teach_mv_download);
        mShare.setOnClickListener(this);
        mReward.setOnClickListener(this);
        mDownLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_share:
                View view = LayoutInflater.from(getContext()).inflate(R.layout.share_pou_window, null);
                PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(view);

                break;
            case R.id.teach_mv_reward:
                break;
            case R.id.teach_mv_download:
                break;
        }
    }
}
