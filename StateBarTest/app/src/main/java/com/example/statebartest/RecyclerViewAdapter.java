package com.example.statebartest;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<String> mPath;
    public RecyclerViewAdapter(List<String> path, Context context) {
        mContext=context;
        mPath=path;

    }

    public void setPath(List<String> path) {
        mPath = path;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mImageView.setImageURI(Uri.fromFile(new File(mPath.get(position))));
    }

    @Override
    public int getItemCount() {
        return mPath.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.image_item);
        }

    }
}
