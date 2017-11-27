package com.example.exercise5.rest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.exercise5.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Roee on 25/11/2017.
 */

public class HitsAdapter extends RecyclerView.Adapter<HitsAdapter.HitViewHolder> {

    private ArrayList<Hit> hits;

    public HitsAdapter (ArrayList<Hit> hitsList) {
        this.hits = hitsList;
    }

    @Override
    public HitViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hit_layout, parent, false);
        ImageView hitImage = view.findViewById(R.id.hit_image);
        HitViewHolder hitViewHolder = new HitViewHolder(view, hitImage);
        return hitViewHolder;
    }

    @Override
    public void onBindViewHolder (HitViewHolder holder, int position) {
        Hit hit = hits.get(position);
        Picasso.with(holder.hitLayout.getContext()).load(hit.getWebformatURL()).placeholder(R.drawable.loading).into(holder.hitImageView);
    }

    @Override
    public int getItemCount () {
        return hits.size();
    }

    class HitViewHolder extends RecyclerView.ViewHolder {

        View hitLayout;
        ImageView hitImageView;

        HitViewHolder (View itemView, ImageView imageView) {
            super(itemView);
            hitLayout = itemView;
            hitImageView = imageView;
        }
    }

}
