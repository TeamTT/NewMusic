package com.example.newmusic.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.model.MvCommentMode;
import com.example.newmusic.model.MvMode;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class MvCommentAdapter extends BaseAdapter {
    private List<MvCommentMode> data;
    private LayoutInflater inflater;
    private ImageOptions options;

    public MvCommentAdapter(Context context, List<MvCommentMode> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
        inflater = LayoutInflater.from(context);
        options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
    }

    public void updateRes(List<MvCommentMode> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addRes(List<MvCommentMode> data) {
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
    public MvCommentMode getItem(int position) {
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
            convertView = inflater.inflate(R.layout.mv_comment_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MvCommentMode commentMode = getItem(position);
        CharSequence format = DateFormat.format("yyyy-MM-dd", Long.parseLong(commentMode.getRegtime()));
        holder.date.setText(format);
        holder.name.setText(commentMode.getNickname());
        holder.content.setText(commentMode.getContent());
        x.image().bind(holder.image, commentMode.getFace(),options);
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView content;
        TextView date;

        public ViewHolder(View itemView) {
            image = (ImageView) itemView.findViewById(R.id.mv_comment_item_image);
            name = (TextView) itemView.findViewById(R.id.mv_comment_item_name);
            content = (TextView) itemView.findViewById(R.id.mv_comment_item_content);
            date = (TextView) itemView.findViewById(R.id.mv_comment_item_date);
        }
    }
}
