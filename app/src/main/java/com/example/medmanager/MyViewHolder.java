package com.example.medmanager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
public TextView mediName;
public TextView desc;

public ImageView image;



public MyViewHolder(View itemView) {
    super(itemView);
    this.mediName = itemView.findViewById(R.id.foodPopulaerName);
    this.desc = itemView.findViewById(R.id.foodPopulaerName);
    this.image = itemView.findViewById(R.id.foodImage);

}

}
