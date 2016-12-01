package com.example.newmusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.model.LocationMvMode;
import com.example.newmusic.model.MvMode;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class LocationMvAdapter extends BaseAdapter {
    private List<LocationMvMode> data;
    private LayoutInflater inflater;

    public LocationMvAdapter(Context context, List<LocationMvMode> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
        inflater = LayoutInflater.from(context);
    }

    public void updateRes(List<LocationMvMode> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addRes(List<LocationMvMode> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public LocationMvMode getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.location_mv_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocationMvMode item = getItem(position);
        holder.content.setText(item.getContent());
        holder.pb.setProgress(item.getProgress());
        x.image().bind(holder.image,item.getImage() );
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView content;
        ProgressBar pb;

        public ViewHolder(View itemView) {
            image = (ImageView) itemView.findViewById(R.id.teach_location_mv_item_image);
            content = (TextView) itemView.findViewById(R.id.teach_location_mv_item_content);
            pb = (ProgressBar) itemView.findViewById(R.id.teach_location_mv_item_progressBar);
        }
    }
}
