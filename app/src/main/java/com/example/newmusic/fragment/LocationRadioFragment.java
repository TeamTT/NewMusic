package com.example.newmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newmusic.MusicDetailsActivity;
import com.example.newmusic.R;
import com.example.newmusic.adapter.MyMusicListAdapter;
import com.example.newmusic.contants.HttpParams;
import com.example.newmusic.model.Mp3Info;
import com.example.newmusic.model.RadioDetailModel;
import com.example.newmusic.utils.medieUtils;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/27.
 */
public class LocationRadioFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = LocationRadioFragment.class.getSimpleName();
    private ArrayList<Mp3Info> mp3Infos;
    private ArrayList<RadioDetailModel> data;

    private MyMusicListAdapter adapter;

    private View layout;
    private ListView lv;
    private ImageView mImageView;
    private TextView mTitle;
    private TextView mSingler;
    private ImageView mPlay;
    private ImageView mNext;
    private LinearLayout mLinearLayout;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_location_radio, container, false);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        bindPlayService();

        for (int i = 0; i < mp3Infos.size(); i++) {
            Mp3Info mp3Info = mp3Infos.get(i);
            String title = mp3Info.getTitle();
            String artist = mp3Info.getArtist();
            String url = mp3Info.getUrl();
            RadioDetailModel detailModel = new RadioDetailModel(url, "", "", title, artist);
            data.add(detailModel);
        }

        if (playService != null && playService.isplay()) {

            int position = playService.getCurrentIndex();

            if (data.size() != 0) {

                Log.e(TAG, "change: " + position);

                RadioDetailModel detailModel = data.get(position);

                x.image().bind(mImageView, detailModel.getImg());

                mTitle.setText(detailModel.getSongName());

                mSingler.setText(detailModel.getSinglerName());
            }
        }

        if (playService != null && playService.isplay()) {
            mPlay.setImageResource(R.mipmap.pause);
        } else {
            mPlay.setImageResource(R.mipmap.play);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unbindPlayService();
    }

    private void initView() {
        lv = (ListView) layout.findViewById(R.id.fragment_location_listview);
        mImageView = ((ImageView) layout.findViewById(R.id.fragment_radio_image));
        mTitle = ((TextView) layout.findViewById(R.id.fragment_radio_title));
        mSingler = ((TextView) layout.findViewById(R.id.fragment_radio_singler));
        mPlay = ((ImageView) layout.findViewById(R.id.fragment_radio_image_play));
        mNext = ((ImageView) layout.findViewById(R.id.fragment_radio_image_next));
        mLinearLayout = ((LinearLayout) layout.findViewById(R.id.fragment_radio_LinearLayout));

        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        mSingler.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        mLinearLayout.setVisibility(View.GONE);


        mp3Infos = medieUtils.getMp3Infos(getActivity());

        adapter = new MyMusicListAdapter(getActivity(), mp3Infos);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG, "onResume: " + playService);

        if (playService != null) {
            playService.setInfos(data);
        }

        playService.play(position);

        this.position = position;

        mLinearLayout.setVisibility(View.VISIBLE);
        mSingler.setText(data.get(position).getSinglerName());
        mTitle.setText(data.get(position).getSongName());

    }

    @Override
    public void publish(int progress) {

    }

    @Override
    public void change(int position) {
        mSingler.setText(data.get(position).getSinglerName());
        mTitle.setText(data.get(position).getSongName());
    }

    @Override
    public void progreses(int progress) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_radio_image_play:
                if (playService.isplay()) {
                    playService.pose();
                    ((ImageView) v).setImageResource(R.mipmap.play);
                } else {
                    playService.start();
                    ((ImageView) v).setImageResource(R.mipmap.pause);
                }
                break;

            case R.id.fragment_radio_image_next:
                next(position);
                break;

            case R.id.fragment_radio_title:
            case R.id.fragment_radio_singler:
            case R.id.fragment_radio_image:
                HttpParams.SIZE = data.size();

                Intent intent = new Intent(getActivity(), MusicDetailsActivity.class);
                for (int i = 0; i < HttpParams.SIZE; i++) {
                    intent.putExtra("model" + i, data.get(i));
                }
                intent.putExtra("position", position);
                intent.putExtra("flag", playService.isplay());
                intent.putExtra("Tag", playService.getPlay_mode());
                startActivity(intent);

                break;
        }

    }

    public void next(int position) {

        position++;

        playService.play(position);

    }
}
