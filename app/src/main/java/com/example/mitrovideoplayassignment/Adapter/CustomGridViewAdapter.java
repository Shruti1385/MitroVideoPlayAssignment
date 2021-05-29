package com.example.mitrovideoplayassignment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mitrovideoplayassignment.Model.VideoDetailsModel;
import com.example.mitrovideoplayassignment.R;
import com.example.mitrovideoplayassignment.Screens.ExoPlayerActivity;

import java.util.ArrayList;

public class CustomGridViewAdapter extends RecyclerView.Adapter<CustomGridViewAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<VideoDetailsModel> videoArrayList;
    ArrayList<VideoDetailsModel > videoArrayFullList;

    public CustomGridViewAdapter(Context context, ArrayList<VideoDetailsModel> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
        videoArrayFullList = new ArrayList<>(videoArrayList);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)  {
        // set the data in items
        holder.fileName.setText(videoArrayList.get(position).getVideoName());
        holder.fileDate.setText(videoArrayList.get(position).getVideoCreatedDate());
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoArrayList.get(position).getVideoUri().toString(), MediaStore.Video.Thumbnails.MINI_KIND);
        holder.thumbnail.setImageBitmap(bitmap);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, ExoPlayerActivity.class);
                intent.putExtra("position", position); // pass the selected item position
                intent.putParcelableArrayListExtra("VideoArrayList", videoArrayList);
                context.startActivity(intent); // start Intent
            }
        });
    }
    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView fileName,fileDate;
        ImageView thumbnail;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            fileName = (TextView) itemView.findViewById(R.id.video_file_name);
            fileDate = (TextView) itemView.findViewById(R.id.video_file_date);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }
    private Filter listFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<VideoDetailsModel >  filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(videoArrayFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (VideoDetailsModel item : videoArrayFullList) {
                    if (item.getVideoName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            videoArrayList.clear();
            videoArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };
}

