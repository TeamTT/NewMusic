package com.example.newmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.newmusic.adapter.MusicDetailViewpageAdapter;
import com.example.newmusic.adapter.RadioRecyclerAdapter;
import com.example.newmusic.contants.HttpParams;
import com.example.newmusic.fragment.MusicDetailFragment;
import com.example.newmusic.fragment.MusicDetailLyricFragment;
import com.example.newmusic.model.RadioDetailModel;

import java.util.ArrayList;
import java.util.List;

public class MusicDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private MusicDetailViewpageAdapter adapter;
    public static List<RadioDetailModel> data;
    public static int position;
    private ImageView mChose;
    private ImageView mLast;
    private ImageView mPlay;
    private ImageView mNext;
    private ImageView mComm;
    private RadioRecyclerAdapter.SendPosition sendPosition;
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    private Button mButton;

    public void setSendPosition(RadioRecyclerAdapter.SendPosition sendPosition) {
        this.sendPosition = sendPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_details);

        data = new ArrayList<>();

        for (int i = 0; i < HttpParams.SIZE; i++) {
            data.add((RadioDetailModel) getIntent().getSerializableExtra("model" + i));
        }

        position = getIntent().getIntExtra("position", 0);

        initView();

        switch (getIntent().getIntExtra("Tag", 0)) {
            case PlayService.ORDER_PLAY:
                mChose.setImageResource(R.mipmap.normal);
                mChose.setTag(PlayService.ORDER_PLAY);
                break;
            case PlayService.RANDOM_PLAY:
                mChose.setImageResource(R.mipmap.random);
                mChose.setTag(PlayService.RANDOM_PLAY);
                break;
            case PlayService.SINGLE_PLAY:
                mChose.setImageResource(R.mipmap.onlyome);
                mChose.setTag(PlayService.SINGLE_PLAY);
                break;
        }

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.activity_music_details_ViewPager);

        mChose = (ImageView) findViewById(R.id.activity_music_details_image1);
        mLast = (ImageView) findViewById(R.id.activity_music_details_image2);
        mPlay = (ImageView) findViewById(R.id.activity_music_details_image3);
        mNext = (ImageView) findViewById(R.id.activity_music_details_image4);
        mComm = (ImageView) findViewById(R.id.activity_music_details_image5);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_music_details_LinearLayout);
        mEditText = (EditText) findViewById(R.id.activity_music_details_EditText);
        mButton = (Button) findViewById(R.id.activity_music_details_Button);

        mLinearLayout.setVisibility(View.GONE);

        boolean flag = getIntent().getBooleanExtra("flag", true);

        if (flag) {
            mPlay.setImageResource(R.mipmap.pasuesc);
        } else {
            mPlay.setImageResource(R.mipmap.playsc);
        }

        mChose.setOnClickListener(this);
        mLast.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mComm.setOnClickListener(this);
        mButton.setOnClickListener(this);

        adapter = new MusicDetailViewpageAdapter(this.getSupportFragmentManager(), getData());
        mViewPager.setAdapter(adapter);
    }

    public List<Fragment> getData() {

        List<Fragment> data = new ArrayList<>();

        data.add(new MusicDetailFragment());

        data.add(new MusicDetailLyricFragment());

        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_music_details_image1:
                sendPosition.sendPlayMode(((ImageView) v));
                break;
            case R.id.activity_music_details_image2:
                HttpParams.isOnPush = false;
                if (position == 0) {
                    position = data.size() - 1;
                } else {
                    position--;
                }
                sendPosition.sendForPlay(position);
                break;
            case R.id.activity_music_details_image3:
                sendPosition.sendImageView(((ImageView) v));
                break;
            case R.id.activity_music_details_image4:
                HttpParams.isOnPush = false;
                if (position == data.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                sendPosition.sendForPlay(position);
                break;
            case R.id.activity_music_details_image5:
                mLinearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_music_details_Button:
                String result = mEditText.getText().toString();

                sendPosition.sendCommStr(result);

                mEditText.setText("");

                mLinearLayout.setVisibility(View.GONE);

                break;
        }
    }
}
