package com.example.newmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.model.Mp3Info;
import com.example.newmusic.utils.medieUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/22.
 */
public class MyMusicListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Mp3Info> mp3Infos;

    public MyMusicListAdapter(Context context, ArrayList<Mp3Info> mp3Infos) {
        this.context = context;
        this.mp3Infos = mp3Infos;
    }

    public void setMp3Infos(ArrayList<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
    }

    @Override
    public int getCount() {
        return mp3Infos.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3Infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_music_list,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        Mp3Info mp3Info=mp3Infos.get(position);

        holder.item_tv_music.setText(mp3Info.getTitle());

        if (mp3Info.getArtist() != null) {
            holder.item_tv_name.setText(mp3Info.getArtist());
        } else {
            holder.item_tv_name.setText("未知歌手");
        }

        holder.item_tv_duration.setText(medieUtils.formatTime(mp3Info.getDuration()));

        return convertView;
    }

    static class ViewHolder{
        TextView item_tv_music,item_tv_name,item_tv_duration;

        public ViewHolder(View convertView) {
            item_tv_name= (TextView) convertView.findViewById(R.id.item_tv_name);
            item_tv_music= (TextView) convertView.findViewById(R.id.item_tv_music);
            item_tv_duration= (TextView) convertView.findViewById(R.id.item_tv_duration);
        }
    }

}
