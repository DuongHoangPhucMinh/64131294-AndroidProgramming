package com.example.vd_recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LandScapeAdapter extends RecyclerView.Adapter<LandScapeAdapter.ItemLandHodler>{
    Context context;
    ArrayList<LandScape> lstData;

    public LandScapeAdapter(Context context, ArrayList<LandScape> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public ItemLandHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater cai_bom = LayoutInflater.from(context);
        View vItem = cai_bom.inflate(R.layout.item_land,parent,false);
        ItemLandHodler viewholderCreated = new ItemLandHodler(vItem);
        return viewholderCreated;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLandHodler holder, int position) {
        LandScape landScapeHienThi = lstData.get(position);
        String caption = landScapeHienThi.getLandCation();
        String tenFileAnh = landScapeHienThi.getLandImageFileName();
        holder.tvCaption.setText(caption);
            String packageName = holder.itemView.getContext().getPackageName();
            int imageID =  holder.itemView.getResources().getIdentifier(tenFileAnh, "mipmap", packageName);
        holder.ivLandscape.setImageResource(imageID);
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    class ItemLandHodler extends RecyclerView.ViewHolder{
        TextView tvCaption;
        ImageView ivLandscape;

        public ItemLandHodler(@NonNull View itemView) {
            super(itemView);
            tvCaption = itemView.findViewById(R.id.textViewCation);
            ivLandscape = itemView.findViewById(R.id.imageViewLand);

        }
    }
}
