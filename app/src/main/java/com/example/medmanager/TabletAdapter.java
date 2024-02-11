package com.example.medmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.DetailActivity;
import com.example.medmanager.MyViewHolder;
import com.example.medmanager.R;





public class TabletAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private String[] names;
    private String[] desc;

    private String[] img_urls;

    private int[] image1;

    private Context context;

    public TabletAdapter(Context context, String[] names, String[] desc,int[] image1) {
        this.context = context;
        this.names = names;
        this.desc = desc;
       this.image1=image1;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemcard, parent, false);
        return new com.example.medmanager.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String item_name = names[position];
        final String item_desc = names[position];
        final int imageResId = image1[position];


        holder.mediName.setText(item_name);
        holder.desc.setText(item_desc);
        holder.image.setImageResource(imageResId);
        // Bind data to views in the ViewHolder if needed

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open DetailActivity with item data
                Intent intent = new Intent(context, DetailActivity.class);
         //       intent.putExtra("item_data", );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;// Return the number of items in the data array
    }
}
