package com.example.exercise5.rest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.exercise5.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Roee on 25/11/2017.
 */

public class HitsAdapter extends RecyclerView.Adapter<HitsAdapter.HitViewHolder> {

    ArrayList<Hit> mHits;

    public HitsAdapter (ArrayList<Hit> mHits) {
        this.mHits = mHits;
    }

    @Override
    public HitViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LinearLayout hitLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.hit_layout, parent, false);
        ImageView hitImage = hitLayout.findViewById(R.id.hit_image);
        HitViewHolder hitViewHolder = new HitViewHolder(hitLayout, hitImage);
        return hitViewHolder;
    }

    @Override
    public void onBindViewHolder (HitViewHolder holder, int position) {
        Hit hit = mHits.get(position);
        Picasso.with(holder.hitLayout.getContext()).load(hit.getWebformatURL()).placeholder(R.drawable.loading).into(holder.hitImageView);
    }

    @Override
    public int getItemCount () {
        return mHits.size();
    }

    public class HitViewHolder extends RecyclerView.ViewHolder {

        LinearLayout hitLayout;
        ImageView hitImageView;

        public HitViewHolder (LinearLayout itemView, ImageView imageView) {
            super(itemView);
            hitLayout = itemView;
            hitImageView = imageView;
        }
    }

}
