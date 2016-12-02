package com.example.newmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newmusic.R;
import com.example.newmusic.model.RadioDetailModel;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RadioRecyclerAdapter extends RecyclerView.Adapter<RadioRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = RadioRecyclerAdapter.class.getSimpleName();
    private List<RadioDetailModel> data;

    private LayoutInflater inflater;

    private SendPosition sendPosition;

    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public RadioRecyclerAdapter(List<RadioDetailModel> data, Context context, SendPosition sendPosition) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
        inflater = LayoutInflater.from(context);
        this.sendPosition = sendPosition;
    }

    public void updataRes(List<RadioDetailModel> data) {
        if (data != null) {
            this.data.clear();

            for (int i = 0; i < data.size(); i++) {

                Log.e(TAG, "handleMessage: " + data.get(i).getSongName());
            }


            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void adddataRes(List<RadioDetailModel> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.fragment_radio_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.fragment_radio_item_image, data.get(position).getImg());
        holder.fragment_radio_item_singler.setText(data.get(position).getSinglerName());
        holder.fragment_radio_item_title.setText(data.get(position).getSongName());

        holder.fragment_radio_item_image_download.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

        if (recyclerView != null) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(v);
            sendPosition.sendForPlay(childAdapterPosition);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fragment_radio_item_title, fragment_radio_item_singler;

        ImageView fragment_radio_item_image, fragment_radio_item_image_download, fragment_radio_item_image_play;

        public ViewHolder(View itemView) {
            super(itemView);

            fragment_radio_item_image = (ImageView) itemView.findViewById(R.id.fragment_radio_item_image);
            fragment_radio_item_image_download = (ImageView) itemView.findViewById(R.id.fragment_radio_item_image_download);
            fragment_radio_item_image_play = (ImageView) itemView.findViewById(R.id.fragment_radio_item_image_play);
            fragment_radio_item_title = (TextView) itemView.findViewById(R.id.fragment_radio_item_title);
            fragment_radio_item_singler = (TextView) itemView.findViewById(R.id.fragment_radio_item_singler);

        }
    }

    public interface SendPosition {
        void sendForDownload(int position);

        void sendForPlay(int position);

        void sendImageView(ImageView imageView);

        void sendPlayMode(ImageView imageView);

        void sendCommStr(String CommStr);

    }


}
